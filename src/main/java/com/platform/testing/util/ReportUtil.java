package com.platform.testing.util;

import com.platform.testing.entity.TestJenkins;
import org.springframework.util.StringUtils;

/**
 * @program: ReportUtil
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-23 12:09
 **/
public class ReportUtil {
    public static String getAllureReportUrl(String buildUrl,
                                            TestJenkins testJenkins,
                                            boolean autoLoginJenkinsFlag) {
        if (StringUtils.isEmpty(buildUrl) || !buildUrl.contains("/job")) {
            return buildUrl;
        }

        String allureReportUrl = buildUrl;
        if (autoLoginJenkinsFlag) {
            allureReportUrl = getAllureReportUrlAndLogin(buildUrl, testJenkins);
        }
        return allureReportUrl + "allure/";

    }

    private static String getAllureReportUrlAndLogin(String buildUrl,
                                                     TestJenkins testJenkins) {
        String allureReportUrl;

        String allureReportBaseUrl = testJenkins.getUrl();
        allureReportUrl = allureReportBaseUrl + buildUrl.substring(buildUrl.indexOf("/job"));
        return allureReportUrl;
    }
}
