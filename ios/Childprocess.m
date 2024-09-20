#import "Childprocess.h"
#import <React/RCTUtils.h>

@implementation Childprocess

NSMutableDictionary *tasks;

- (id)init {
	if (self = [super init]) {
		tasks = [NSMutableDictionary dictionary];
	}
	return self;
}

+ (BOOL)requiresMainQueueSetup
{
	return YES;
}

RCT_EXPORT_MODULE()

RCT_REMAP_METHOD(spawn,
		spawnWithCmd:
		(nonnull NSString*)cmd
	withArguments:(nonnull NSArray*)arguments
	withOptions:(nonnull NSDictionary*)options
	withResolver:(RCTPromiseResolveBlock)resolve
	withRejecter:(RCTPromiseRejectBlock)reject
) {
	NSNumber *cmdID = [self executeCommand:cmd arguments:arguments options:options];
	if (cmdID < (NSNumber *) 0) {
		reject(@"failed", @"execute command failed", RCTErrorWithMessage(@"execute command failed"));
	} else {
		resolve(cmdID);
	}
}

RCT_REMAP_METHOD(kill,
		killWithCmdID:
		(nonnull NSNumber*)cmdID
	withResolver:(RCTPromiseResolveBlock)resolve
	withRejecter:(RCTPromiseRejectBlock)reject
) {
	
}

- (NSArray<NSString *> *)supportedEvents {
	return @[@"stdout", @"stderr", @"terminate"];
}

- (NSNumber *)executeCommand:(NSString *)cmd arguments:(NSArray *)arguments options:(NSDictionary *)options {
	NSNumber *cmdID = @(++_ID_INC);

	return cmdID;
}

@end
