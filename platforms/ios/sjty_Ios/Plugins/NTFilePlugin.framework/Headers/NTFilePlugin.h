/*********************************************************************
 * 版权所有 (C)2013 南天电脑有限公司
 *
 * 文件名称：  NTFilePlugin.h
 * 内容摘要：  给页面使用的文件上传插件
 * 其它说明：
 * 作   者：  古秀湖
 * 完成日期：  2014年12月28日
 **********************************************************************/

#import <Cordova/CDVPlugin.h>

@interface NTFilePlugin : CDVPlugin

/**
 *  上传文件
 *
 *  @param command 包含页面传递的参数
 */
- (void)fileUpload:(CDVInvokedUrlCommand*)command;

@end
