package com.alibaba.cms.sdk.sample;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.cms.model.v20180308.*;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author he.dong
 * @date 2018/6/4
 */
public class AlarmSdkSample {
    private static final Logger logger = LoggerFactory.getLogger(AlarmSdkSample.class);
    private static String accessKeyId = "<accessKeyId>";
    private static String accessKeySecret = "<accessKeySecret>";
    private static final String REGION_ID_BEIJING = "cn-beijing";

    /**
     * 创建报警规则,<br/>
     * 为了简化调用，只暴露了部分参数可配置。<br/>
     * 具体参数设置见 https://help.aliyun.com/document_detail/28619.html?spm=a2c4g.11186623.6.677.7FtWb7
     */
    public static String createAlarm(String alarmName, String namespace, String metricName, String dimensions,
                                   String statistics, String comparisonOperator, String threshold) {
        IClientProfile profile = DefaultProfile.getProfile(REGION_ID_BEIJING, accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        CreateAlarmRequest request = new CreateAlarmRequest();
        request.setAcceptFormat(FormatType.JSON);
        //必选
        request.setName(alarmName);
        //必选，Namespace
        request.setNamespace(namespace);
        //必选
        request.setMetricName(metricName);
        //必选, 报警规则对应实例列表，为json array对应的string，例如[{"instanceId":"name1"},{"instanceId":"name2"}]
        request.setDimensions(dimensions);
        //必选，设置联系组，必须在控制台上已创建, 为json array对应的string，例如 ["联系组1","联系组2"]
        request.setContactGroups("[\"your_group\"]");
        //必选，设置统计方法，必须与定义的metric一致，例如Average
        request.setStatistics(statistics);
        //可选，设置查询周期，单位为s,只能设置成：60, 300, 900,60*N
        request.setPeriod(60);
        //必选，设置报警比较符，只能为以下几种<=,<,>,>=,==,!=
        request.setComparisonOperator(comparisonOperator);
        //必选，设置报警阈值,目前只开放数值类型功能
        request.setThreshold(threshold);
        //可选，设置连续探测几次都满足阈值条件时报警，默认3次
        request.setEvaluationCount(3);
        //可选，报警生效时间的开始时间，默认0，代表0点
        request.setStartTime(1);
        //可选，报警生效时间的结束时间，默认24，代表24点
        request.setEndTime(22);
        //可选，通道沉默周期,默认86400，单位s，只能选5min，10min，15min，30min，60min，3h，6h，12h，24h
        request.setSilenceTime(300);
        //可选，为0是旺旺+邮件，为1是旺旺+邮件+短信
        request.setNotifyType(0);
        //可选，回调url.
        request.setWebhook("{\"url\":\"https://www.abc.com/xxx/\",\"method\":\"get\",\"params\":{}}");

        try {
            logger.info("sending CreateAlarmRequest...");
            CreateAlarmResponse response = client.getAcsResponse(request);
            logger.info("CreateAlarmResponse:\n{}", JSON.toJSONString(response,true));
            return response.getData();
        } catch (ClientException e) {
            logger.info(e.getMessage());
        }

        return null;
    }

    /**
     * 修改报警规则<br/>
     * 具体参数设置见 https://help.aliyun.com/document_detail/28619.html?spm=a2c4g.11186623.6.677.7FtWb7
     */
    public static void updateAlarm(String alarmId) {
        IClientProfile profile = DefaultProfile.getProfile(REGION_ID_BEIJING, accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        UpdateAlarmRequest request = new UpdateAlarmRequest();
        request.setAcceptFormat(FormatType.JSON);

        //必选
        request.setId(alarmId);
        //可选，报警规则名称
        request.setName("alarm-name-update");
        //可选，设置联系组，为json array对应的string，例如 ["联系组1","联系组2"]
        request.setContactGroups("[\"your_group\"]");
        //可选，设置统计方法，必须与定义的metric一致，例如Average
        request.setStatistics("Minimum");
        //可选，设置查询周期，单位为s,只能设置成：60, 300, 900,60*N
        request.setPeriod(900);
        //可选，设置报警比较符，只能为以下几种<=,<,>,>=,==,!=
        request.setComparisonOperator(">=");
        //可选，设置报警阈值,目前只开放数值类型功能
        request.setThreshold("100");
        //可选，设置连续探测几次都满足阈值条件时报警，默认3次
        request.setEvaluationCount(5);
        //可选，报警生效时间的开始时间，默认0，代表0点
        request.setStartTime(9);
        //可选，报警生效时间的结束时间，默认24，代表24点
        request.setEndTime(18);
        //可选，通道沉默周期,默认86400，单位s，只能选5min，10min，15min，30min，60min，3h，6h，12h，24h
        request.setSilenceTime(86400);
        //可选，为0是旺旺+邮件，为1是旺旺+邮件+短信
        request.setNotifyType(1);
        //可选，回调url.
        request.setWebhook("{\"url\":\"https://www.abc.com/xxx/\",\"method\":\"get\",\"params\":{}}");

        try {
            logger.info("sending UpdateAlarmRequest...");
            UpdateAlarmResponse response = client.getAcsResponse(request);
            logger.info("UpdateAlarmResponse:\n{}", JSON.toJSONString(response,true));
        } catch (ClientException e) {
            logger.info(e.getMessage());
        }
    }

    /**
     * 禁用报警规则。报警规则停止后，将停止探测关联实例的监控项数据
     */
    public static void disableAlarm(String alarmId) {
        IClientProfile profile = DefaultProfile.getProfile(REGION_ID_BEIJING, accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        DisableAlarmRequest request = new DisableAlarmRequest();
        request.setAcceptFormat(FormatType.JSON);

        request.setId(alarmId);

        try {
            logger.info("sending DisableAlarmRequest...");
            DisableAlarmResponse response = client.getAcsResponse(request);
            logger.info("DisableAlarmResponse:\n{}", JSON.toJSONString(response,true));
        } catch (ClientException e) {
            logger.info(e.getMessage());
        }
    }

    /**
     * 启动报警规则，当您的报警规则处于停止状态时，可以使用此接口启用报警规则
     */
    public static void enableAlarm(String alarmId) {
        IClientProfile profile = DefaultProfile.getProfile(REGION_ID_BEIJING, accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        EnableAlarmRequest request = new EnableAlarmRequest();
        request.setAcceptFormat(FormatType.JSON);

        request.setId(alarmId);

        try {
            logger.info("sending EnableAlarmRequest...");
            EnableAlarmResponse response = client.getAcsResponse(request);
            logger.info("EnableAlarmResponse:\n{}", JSON.toJSONString(response,true));
        } catch (ClientException e) {
            logger.info(e.getMessage());
        }
    }

    /**
     * 获取报警规则列表
     * */
    public static void listAlarm(String alarmId) {
        IClientProfile profile = DefaultProfile.getProfile(REGION_ID_BEIJING, accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        ListAlarmRequest request = new ListAlarmRequest();
        request.setAcceptFormat(FormatType.JSON);
        //所有参数均为可选参数，根据输入的参数进行结果过滤
        //报警规则的id
        request.setId(alarmId);
        //报警规则名称，支持模糊查询
        //request.setName("keyword");
        // 对应产品的project名称 https://help.aliyun.com/document_detail/28619.html?spm=a2c4g.11186623.6.677.7FtWb7#h2--ecs-2
        //request.setNamespace("acs_ecs_dashboard");
        //规则关联的实例信息，为json object对应的字符串，可以查询到关联该实例的所有规则，应用该字段时必须指定namespace.例如{"instanceId":"name1"}
        //request.setDimension("{\"instanceId\":\"instance_A_id\"}");
        //报警规则状态：ALARM(报警)、INSUFFICIENT_DATA（数据不足）、OK（正常）
        //request.setState("OK");
        //报警启用状态：true为启用，false为禁用
        //request.setIsEnable(true);

        request.setPageNumber(1);
        request.setPageSize(5);

        try {
            logger.info("sending ListAlarmRequest...");
            ListAlarmResponse response = client.getAcsResponse(request);
            logger.info("ListAlarmResponse:\n{}", JSON.toJSONString(response,true));
        } catch (ClientException e) {
            logger.info(e.getMessage());
        }
    }


    /**
     * 删除已创建的报警规则
     */
    public static void deleteAlarm(String alarmId) {
        IClientProfile profile = DefaultProfile.getProfile(REGION_ID_BEIJING, accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        DeleteAlarmRequest request = new DeleteAlarmRequest();
        request.setAcceptFormat(FormatType.JSON);

        request.setId(alarmId);

        try {
            logger.info("sending DeleteAlarmRequest...");
            DeleteAlarmResponse response = client.getAcsResponse(request);
            logger.info("DeleteAlarmResponse:\n{}", JSON.toJSONString(response,true));
        } catch (ClientException e) {
            logger.info(e.getMessage());
        }
    }


    /**
     * 查询联系人组
     */
    public static void listContactGroup() {
        IClientProfile profile = DefaultProfile.getProfile(REGION_ID_BEIJING, accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        ListContactGroupRequest request = new ListContactGroupRequest();
        request.setAcceptFormat(FormatType.JSON);

        request.setPageNumber(3);
        request.setPageSize(100);

        try {
            logger.info("sending ListContactGroupRequest...");
            ListContactGroupResponse response = client.getAcsResponse(request);
            logger.info("ListContactGroupResponse:\n{}", JSON.toJSONString(response,true));
        } catch (ClientException e) {
            logger.info(e.getMessage());
        }
    }

    /**
     * 查询联系人组
     */
    public static void getContacts() {
        IClientProfile profile = DefaultProfile.getProfile(REGION_ID_BEIJING, accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        GetContactsRequest request = new GetContactsRequest();
        request.setAcceptFormat(FormatType.JSON);

        request.setGroupName("your_group_name");

        try {
            logger.info("sending GetContactsRequest...");
            GetContactsResponse response = client.getAcsResponse(request);
            logger.info("GetContactsResponse:\n{}", JSON.toJSONString(response,true));
        } catch (ClientException e) {
            logger.info(e.getMessage());
        }
    }

    /**
     * 查询联系人组
     */
    public static void describeContact() {
        IClientProfile profile = DefaultProfile.getProfile(REGION_ID_BEIJING, accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        DescribeContactRequest request = new DescribeContactRequest();
        request.setAcceptFormat(FormatType.JSON);

        request.setContactName("your_contact_name");

        try {
            logger.info("sending DescribeContactRequest...");
            DescribeContactResponse response = client.getAcsResponse(request);
            logger.info("DescribeContactResponse:\n{}", JSON.toJSONString(response,true));
        } catch (ClientException e) {
            logger.info(e.getMessage());
        }
    }

    /**
     * 获取报警历史
     * 可根据需要输入对应字段做过滤
     */
    public static void describeAlarmHistory(String alarmId) {
        IClientProfile profile = DefaultProfile.getProfile(REGION_ID_BEIJING, accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        DescribeAlarmHistoryRequest request = new DescribeAlarmHistoryRequest();
        request.setAcceptFormat(FormatType.JSON);
        //所有参数均为可选参数，根据输入的参数进行结果过滤
        // 应用分组ID
        //request.setGroupId("group_id");
        // 创建报警规则时云监控自动产生的唯一标识
        request.setAlertName(alarmId);
        // 创建报警规则时用户定义的规则名称
        //request.setRuleName("");
        // 报警规则监控的产品
        //request.setNamespace("");
        // 报警规则监控的指标
        //request.setMetricName("");
        //与EndTime最多间隔最长三天，可查询一年之内的数据，目前仅支持timestamp格式的时间
        //request.setStartTime(String.valueOf(System.currentTimeMillis() - 1000 * 60 * 60 * 24));
        //request.setEndTime(String.valueOf(System.currentTimeMillis() - 1000 * 60 * 60 * 24));
        // 查询结果是否只返回结果条数，默认是false
        //request.setOnlyCount(true);
        // 查询结果是否正序返回，默认是false
        //request.setAscending(false);
        // 报警状态,OK是恢复，ALARM是报警
        //request.setState("OK");
        // 报警通知发送状态，0为已通知用户，1为不在生效期未通知，2为处于报警沉默期未通知
        //request.setStatus("0");

        request.setPage(1);
        request.setPageSize(5);

        try {
            logger.info("sending DescribeAlarmHistoryRequest...");
            DescribeAlarmHistoryResponse response = client.getAcsResponse(request);
            logger.info("DescribeAlarmHistoryResponse:\n{}", JSON.toJSONString(response,true));
        } catch (ClientException e) {
            logger.info(e.getMessage());
        }
    }


    public static void main(String[] args) {
        // 1. 查看联系人祖
        listContactGroup();
        getContacts();
        describeContact();
        // 2. 创建报警
        String alarmId = createAlarm("your_alarm_name",
            "acs_ecs_dashboard",
            "CPUUtilization",
            "[{\"instanceId\":\"instance_A_id\"}]",
            "Average",
            ">=",
            "50");
        // 3. 修改报警
        updateAlarm(alarmId);
        // 4. 查看报警
        listAlarm(alarmId);
        // 5. 禁用报警
        disableAlarm(alarmId);
        // 6. 启用报警
        enableAlarm(alarmId);
        // 7. 查看报警历史
        describeAlarmHistory(alarmId);
        // 8. 删除报警
        deleteAlarm(alarmId);
    }

}
