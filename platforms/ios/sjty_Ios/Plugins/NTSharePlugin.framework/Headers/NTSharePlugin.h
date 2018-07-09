//
//  NTSharePlugin.h
//  icCard_Ios
//
//  Created by 古秀湖 on 15/12/1.
//
//

#import <Cordova/CDVPlugin.h>

//新浪微博
#define kAppKey        @"2444270328"
#define kRedirectURI   @"http://youtui.mobi/weiboResponse"

//QQ(此处需要更改）
#define kQQAppID @"1103403951"
#define kQQURI   @"http://youtui.mobi"

//微信
#define kWeiXinKey  @"wx38b6ddd430216b5c"

@interface NTSharePlugin : CDVPlugin
/**
 *  分享
 *
 *  @param command 页面发过来的内容
 */
- (void)share:(CDVInvokedUrlCommand *)command;

//回调
+ (BOOL)handleOpenURL:(NSURL *)url;

@end
