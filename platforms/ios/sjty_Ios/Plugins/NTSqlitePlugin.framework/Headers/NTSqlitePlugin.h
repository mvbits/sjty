//
//  NTSqlite.h
//  icCard_Ios
//
//  Created by 古秀湖 on 15/10/27.
//
//

#import <Cordova/CDVPlugin.h>

@interface NTSqlitePlugin : CDVPlugin

/**
 *  执行sqlite
 *
 *  @param command 页面传来的参数
 */
- (void)execSql:(CDVInvokedUrlCommand *)command;

@end
