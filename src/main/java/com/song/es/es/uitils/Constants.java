package com.song.es.es.uitils;

import java.nio.charset.Charset;
import java.time.format.DateTimeFormatter;

/**
 * @Desc
 * @Author
 * @Date 2019/8/16
 */
public class Constants {
    public static final DateTimeFormatter defaultDateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    public static final DateTimeFormatter prettyDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 一天等于多少秒
     */
    public static final long ONE_DAY_SECONDS = 86400l;

    /**
     * ISO-8859-1编码
     */
    public static final Charset Iso88951 = Charset.forName("ISO-8859-1");

    /**
     * UTF-8 编码
     */
    public static final Charset Utf8 = Charset.forName("UTF-8");

    /**
     * GB18030 编码
     */
    public static final Charset Gb18030 = Charset.forName("GB18030");

    /**
     * 默认自然渠道ID
     */
    public static final long DEFAULT_BRANCH_ID = 999L;

    public static final String profileDev  = "dev";
    public static final String profileTest = "test";
    public static final String profileProd = "prod";

    /**
     * 系统通道的码商ID为-1
     */
    public static final Long SystemChannelSupplierId = -1L;
}
