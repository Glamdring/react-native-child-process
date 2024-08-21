import {NativeModules, NativeEventEmitter} from 'react-native';

interface SpawnOptions {
	pwd?: string,
	stdout?: (output: string) => void,
	stderr?: (err: string) => void,
	terminate?: (code: number) => void,
  synchronous?: boolean,
}

const Childprocess = NativeModules.Childprocess;
const childprocessEmitter = new NativeEventEmitter(Childprocess);

const subscriptions = {};

function onEvent({output, id, event}){
	let subscription = subscriptions[id];
	subscription && subscription[event] && subscription[event](output);
}

childprocessEmitter.addListener(
	'stdout',
	onEvent,
);
childprocessEmitter.addListener(
	'stderr',
	onEvent,
);
childprocessEmitter.addListener(
	'terminate',
	onEvent,
);

export async function spawn(cmd: string, args?: string[], options?: SpawnOptions) {
	if(options == undefined){
		options = {};
	}
	const {pwd, stdout, stderr, terminate, synchronous} = options;
	let opt = {
		pwd,
	};

  try {
    if (options.synchronous) {
      return await Childprocess.spawn(cmd, args, opt);
    } else {
      const cmdID = await Childprocess.spawn(cmd, args, opt);
      subscriptions[cmdID] = {
        stdout,
        stderr,
        terminate: function(payload){
          removeSubscriptions(cmdID);
          terminate && terminate(payload);
        },
      };

      return cmdID;    
    }
  } catch (ex) {
    console.log(ex);
  }
}

function removeSubscriptions(cmdID) {
	let ss = subscriptions[cmdID];
	if (ss) {
		delete subscriptions[cmdID];
	}
}

export async function kill(cmdID:number) {
	removeSubscriptions(cmdID);

	try {
		await Childprocess.kill(cmdID)
	} catch (e) {
		console.log(e);
	}
}
