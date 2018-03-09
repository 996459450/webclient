package com.fx.news;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.PluginConfiguration;
import com.gargoylesoftware.htmlunit.PluginConfiguration.MimeType;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.javascript.configuration.BrowserFeature;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

public class BrowserVersion implements Serializable, Cloneable {
    private static final String NETSCAPE = "Netscape";
    private static final String LANGUAGE_ENGLISH_US = "en-US";
    private static final String CPU_CLASS_X86 = "x86";
    private static final String PLATFORM_WIN32 = "Win32";
    public static final BrowserVersion FIREFOX_45 = new BrowserVersion("Netscape", "5.0 (Windows)", "Mozilla/5.0 (Windows NT 6.1; rv:45.0) Gecko/20100101 Firefox/45.0", 45, "FF45", (BrowserVersionFeatures[])null);
    public static final BrowserVersion INTERNET_EXPLORER = new BrowserVersion("Netscape", "5.0 (Windows NT 6.1; Trident/7.0; rv:11.0) like Gecko", "Mozilla/5.0 (Windows NT 6.1; Trident/7.0; rv:11.0) like Gecko", 11, "IE", (BrowserVersionFeatures[])null);
    public static final BrowserVersion CHROME = new BrowserVersion("Netscape", "5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.76 Safari/537.36", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.76 Safari/537.36", 56, "Chrome", (BrowserVersionFeatures[])null);
    public static final BrowserVersion EDGE = new BrowserVersion("Netscape", "5.0 (Windows NT 10.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586", "Mozilla/5.0 (Windows NT 10.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586", 13, "Edge", (BrowserVersionFeatures[])null);
    public static final BrowserVersion BEST_SUPPORTED;
    private static BrowserVersion DefaultBrowserVersion_;
    private String applicationCodeName_;
    private String applicationMinorVersion_;
    private String applicationName_;
    private String applicationVersion_;
    private String buildId_;
    private String vendor_;
    private String browserLanguage_;
    private String cpuClass_;
    private boolean onLine_;
    private String platform_;
    private String systemLanguage_;
    private String userAgent_;
    private String userLanguage_;
    private int browserVersionNumeric_;
    private final Set<PluginConfiguration> plugins_;
    private final Set<BrowserVersionFeatures> features_;
    private final String nickname_;
    private String htmlAcceptHeader_;
    private String imgAcceptHeader_;
    private String cssAcceptHeader_;
    private String scriptAcceptHeader_;
    private String xmlHttpRequestAcceptHeader_;
    private String[] headerNamesOrdered_;
    private Map<String, String> uploadMimeTypes_;

    public BrowserVersion(String applicationName, String applicationVersion, String userAgent, int browserVersionNumeric) {
        this(applicationName, applicationVersion, userAgent, browserVersionNumeric, applicationName + browserVersionNumeric, (BrowserVersionFeatures[])null);
    }

    public BrowserVersion(String applicationName, String applicationVersion, String userAgent, int browserVersionNumeric, BrowserVersionFeatures[] features) {
        this(applicationName, applicationVersion, userAgent, browserVersionNumeric, applicationName + browserVersionNumeric, features);
    }

    private BrowserVersion(String applicationName, String applicationVersion, String userAgent, int browserVersionNumeric, String nickname, BrowserVersionFeatures[] features) {
        this.applicationCodeName_ = "Mozilla";
        this.applicationMinorVersion_ = "0";
        this.browserLanguage_ = "en-US";
        this.cpuClass_ = "x86";
        this.onLine_ = true;
        this.platform_ = "Win32";
        this.systemLanguage_ = "en-US";
        this.userLanguage_ = "en-US";
        this.plugins_ = new HashSet();
        this.features_ = EnumSet.noneOf(BrowserVersionFeatures.class);
        this.uploadMimeTypes_ = new HashMap();
        this.applicationName_ = applicationName;
        this.setApplicationVersion(applicationVersion);
        this.userAgent_ = userAgent;
        this.browserVersionNumeric_ = browserVersionNumeric;
        this.nickname_ = nickname;
        this.htmlAcceptHeader_ = "*/*";
        this.imgAcceptHeader_ = "*/*";
        this.cssAcceptHeader_ = "*/*";
        this.scriptAcceptHeader_ = "*/*";
        this.xmlHttpRequestAcceptHeader_ = "*/*";
        if(features != null) {
            this.features_.addAll(Arrays.asList(features));
        }

    }

    private void initDefaultFeatures() {
        String expectedBrowserName;
        if(this.isIE()) {
            expectedBrowserName = "IE";
        } else if(this.isFirefox()) {
            expectedBrowserName = "FF";
        } else if(this.isEdge()) {
            expectedBrowserName = "EDGE";
        } else {
            expectedBrowserName = "CHROME";
        }

        BrowserVersionFeatures[] var2 = BrowserVersionFeatures.values();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            BrowserVersionFeatures features = var2[var4];

            try {
                Field e = BrowserVersionFeatures.class.getField(features.name());
                BrowserFeature browserFeature = (BrowserFeature)e.getAnnotation(BrowserFeature.class);
                if(browserFeature != null) {
                    WebBrowser[] var8 = browserFeature.value();
                    int var9 = var8.length;

                    for(int var10 = 0; var10 < var9; ++var10) {
                        WebBrowser browser = var8[var10];
                        if(expectedBrowserName.equals(browser.value().name()) && browser.minVersion() <= this.getBrowserVersionNumeric() && browser.maxVersion() >= this.getBrowserVersionNumeric()) {
                            this.features_.add(features);
                        }
                    }
                }
            } catch (NoSuchFieldException var12) {
                throw new IllegalStateException(var12);
            }
        }

    }

    public static BrowserVersion getDefault() {
        return DefaultBrowserVersion_;
    }

    public static void setDefault(BrowserVersion newBrowserVersion) {
        WebAssert.notNull("newBrowserVersion", newBrowserVersion);
        DefaultBrowserVersion_ = newBrowserVersion;
    }

    public final boolean isIE() {
        return this.getNickname().startsWith("IE");
    }

    public final boolean isChrome() {
        return this.getNickname().startsWith("Chrome");
    }

    public final boolean isEdge() {
        return this.getNickname().startsWith("Edge");
    }

    public final boolean isFirefox() {
        return this.getNickname().startsWith("FF");
    }

    public String getApplicationCodeName() {
        return this.applicationCodeName_;
    }

    public String getApplicationMinorVersion() {
        return this.applicationMinorVersion_;
    }

    public String getApplicationName() {
        return this.applicationName_;
    }

    public String getApplicationVersion() {
        return this.applicationVersion_;
    }

    public String getVendor() {
        return this.vendor_;
    }

    public String getBrowserLanguage() {
        return this.browserLanguage_;
    }

    public String getCpuClass() {
        return this.cpuClass_;
    }

    public boolean isOnLine() {
        return this.onLine_;
    }

    public String getPlatform() {
        return this.platform_;
    }

    public String getSystemLanguage() {
        return this.systemLanguage_;
    }

    public String getUserAgent() {
        return this.userAgent_;
    }

    public String getUserLanguage() {
        return this.userLanguage_;
    }

    public String getHtmlAcceptHeader() {
        return this.htmlAcceptHeader_;
    }

    public String getScriptAcceptHeader() {
        return this.scriptAcceptHeader_;
    }

    public String getXmlHttpRequestAcceptHeader() {
        return this.xmlHttpRequestAcceptHeader_;
    }

    public String getImgAcceptHeader() {
        return this.imgAcceptHeader_;
    }

    public String getCssAcceptHeader() {
        return this.cssAcceptHeader_;
    }

    public void setApplicationCodeName(String applicationCodeName) {
        this.applicationCodeName_ = applicationCodeName;
    }

    public void setApplicationMinorVersion(String applicationMinorVersion) {
        this.applicationMinorVersion_ = applicationMinorVersion;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName_ = applicationName;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion_ = applicationVersion;
    }

    public void setVendor(String vendor) {
        this.vendor_ = vendor;
    }

    public void setBrowserLanguage(String browserLanguage) {
        this.browserLanguage_ = browserLanguage;
    }

    public void setCpuClass(String cpuClass) {
        this.cpuClass_ = cpuClass;
    }

    public void setOnLine(boolean onLine) {
        this.onLine_ = onLine;
    }

    public void setPlatform(String platform) {
        this.platform_ = platform;
    }

    public void setSystemLanguage(String systemLanguage) {
        this.systemLanguage_ = systemLanguage;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent_ = userAgent;
    }

    public void setUserLanguage(String userLanguage) {
        this.userLanguage_ = userLanguage;
    }

    public void setBrowserVersion(int browserVersion) {
        this.browserVersionNumeric_ = browserVersion;
    }

    public void setHtmlAcceptHeader(String htmlAcceptHeader) {
        this.htmlAcceptHeader_ = htmlAcceptHeader;
    }

    public void setImgAcceptHeader(String imgAcceptHeader) {
        this.imgAcceptHeader_ = imgAcceptHeader;
    }

    public void setCssAcceptHeader(String cssAcceptHeader) {
        this.cssAcceptHeader_ = cssAcceptHeader;
    }

    public void setScriptAcceptHeader(String scriptAcceptHeader) {
        this.scriptAcceptHeader_ = scriptAcceptHeader;
    }

    public void setXmlHttpRequestAcceptHeader(String xmlHttpRequestAcceptHeader) {
        this.xmlHttpRequestAcceptHeader_ = xmlHttpRequestAcceptHeader;
    }

    public int getBrowserVersionNumeric() {
        return this.browserVersionNumeric_;
    }

    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o, new String[0]);
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, new String[0]);
    }

    public Set<PluginConfiguration> getPlugins() {
        return this.plugins_;
    }

    public boolean hasFeature(BrowserVersionFeatures property) {
        return this.features_.contains(property);
    }

    public String getNickname() {
        return this.nickname_;
    }

    public String getBuildId() {
        return this.buildId_;
    }

    public String[] getHeaderNamesOrdered() {
        return this.headerNamesOrdered_;
    }

    public void setHeaderNamesOrdered(String[] headerNames) {
        this.headerNamesOrdered_ = headerNames;
    }

    public void registerUploadMimeType(String fileExtension, String mimeType) {
        if(fileExtension != null) {
            this.uploadMimeTypes_.put(fileExtension.toLowerCase(Locale.ROOT), mimeType);
        }

    }

    public String getUploadMimeType(File file) {
        if(file == null) {
            return "";
        } else {
            String fileExtension = FilenameUtils.getExtension(file.getName());
            String mimeType = (String)this.uploadMimeTypes_.get(fileExtension.toLowerCase(Locale.ROOT));
            return mimeType != null?mimeType:"";
        }
    }

    public String toString() {
        return this.nickname_;
    }

    public BrowserVersion clone() {
        BrowserVersion clone = new BrowserVersion(this.getApplicationName(), this.getApplicationVersion(), this.getUserAgent(), this.getBrowserVersionNumeric(), this.getNickname(), (BrowserVersionFeatures[])null);
        clone.setApplicationCodeName(this.getApplicationCodeName());
        clone.setApplicationMinorVersion(this.getApplicationMinorVersion());
        clone.setVendor(this.getVendor());
        clone.setBrowserLanguage(this.getBrowserLanguage());
        clone.setCpuClass(this.getCpuClass());
        clone.setOnLine(this.isOnLine());
        clone.setPlatform(this.getPlatform());
        clone.setSystemLanguage(this.getSystemLanguage());
        clone.setUserLanguage(this.getUserLanguage());
        clone.buildId_ = this.getBuildId();
        clone.htmlAcceptHeader_ = this.getHtmlAcceptHeader();
        clone.imgAcceptHeader_ = this.getImgAcceptHeader();
        clone.cssAcceptHeader_ = this.getCssAcceptHeader();
        clone.scriptAcceptHeader_ = this.getScriptAcceptHeader();
        clone.xmlHttpRequestAcceptHeader_ = this.getXmlHttpRequestAcceptHeader();
        clone.headerNamesOrdered_ = this.getHeaderNamesOrdered();
        Iterator var2 = this.getPlugins().iterator();

        while(var2.hasNext()) {
            PluginConfiguration pluginConf = (PluginConfiguration)var2.next();
            clone.getPlugins().add(pluginConf.clone());
        }

        clone.features_.addAll(this.features_);
        clone.uploadMimeTypes_.putAll(this.uploadMimeTypes_);
        return clone;
    }

    static {
        BEST_SUPPORTED = CHROME;
        DefaultBrowserVersion_ = BEST_SUPPORTED;
        FIREFOX_45.initDefaultFeatures();
        FIREFOX_45.setVendor("");
        FIREFOX_45.buildId_ = "20161129180326";
        FIREFOX_45.setHeaderNamesOrdered(new String[]{"Host", "User-Agent", "Accept", "Accept-Language", "Accept-Encoding", "Referer", "Cookie", "Connection"});
        FIREFOX_45.setHtmlAcceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        FIREFOX_45.setXmlHttpRequestAcceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        FIREFOX_45.setImgAcceptHeader("image/png,image/*;q=0.8,*/*;q=0.5");
        FIREFOX_45.setCssAcceptHeader("text/css,*/*;q=0.1");
        INTERNET_EXPLORER.initDefaultFeatures();
        INTERNET_EXPLORER.setVendor("");
        INTERNET_EXPLORER.setHeaderNamesOrdered(new String[]{"Accept", "Referer", "Accept-Language", "User-Agent", "Accept-Encoding", "Host", "DNT", "Connection", "Cookie"});
        INTERNET_EXPLORER.setHtmlAcceptHeader("text/html, application/xhtml+xml, */*");
        INTERNET_EXPLORER.setImgAcceptHeader("image/png, image/svg+xml, image/*;q=0.8, */*;q=0.5");
        INTERNET_EXPLORER.setCssAcceptHeader("text/css, */*");
        INTERNET_EXPLORER.setScriptAcceptHeader("application/javascript, */*;q=0.8");
        EDGE.initDefaultFeatures();
        EDGE.setVendor("");
        CHROME.initDefaultFeatures();
        CHROME.setApplicationCodeName("Mozilla");
        CHROME.setVendor("Google Inc.");
        CHROME.setPlatform("MacIntel");
        CHROME.setCpuClass((String)null);
        CHROME.setHeaderNamesOrdered(new String[]{"Host", "Connection", "Accept", "User-Agent", "Referer", "Accept-Encoding", "Accept-Language", "Cookie"});
        CHROME.setHtmlAcceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        CHROME.setImgAcceptHeader("image/webp,image/*,*/*;q=0.8");
        CHROME.setCssAcceptHeader("text/css,*/*;q=0.1");
        CHROME.setScriptAcceptHeader("*/*");
        CHROME.registerUploadMimeType("html", "text/html");
        CHROME.registerUploadMimeType("htm", "text/html");
        CHROME.registerUploadMimeType("css", "text/css");
        CHROME.registerUploadMimeType("xml", "text/xml");
        CHROME.registerUploadMimeType("gif", "image/gif");
        CHROME.registerUploadMimeType("jpeg", "image/jpeg");
        CHROME.registerUploadMimeType("jpg", "image/jpeg");
        CHROME.registerUploadMimeType("webp", "image/webp");
        CHROME.registerUploadMimeType("mp4", "video/mp4");
        CHROME.registerUploadMimeType("m4v", "video/mp4");
        CHROME.registerUploadMimeType("m4a", "audio/x-m4a");
        CHROME.registerUploadMimeType("mp3", "audio/mp3");
        CHROME.registerUploadMimeType("ogv", "video/ogg");
        CHROME.registerUploadMimeType("ogm", "video/ogg");
        CHROME.registerUploadMimeType("ogg", "audio/ogg");
        CHROME.registerUploadMimeType("oga", "audio/ogg");
        CHROME.registerUploadMimeType("opus", "audio/ogg");
        CHROME.registerUploadMimeType("webm", "video/webm");
        CHROME.registerUploadMimeType("wav", "audio/wav");
        CHROME.registerUploadMimeType("flac", "audio/flac");
        CHROME.registerUploadMimeType("xhtml", "application/xhtml+xml");
        CHROME.registerUploadMimeType("xht", "application/xhtml+xml");
        CHROME.registerUploadMimeType("xhtm", "application/xhtml+xml");
        CHROME.registerUploadMimeType("txt", "text/plain");
        CHROME.registerUploadMimeType("text", "text/plain");
        FIREFOX_45.registerUploadMimeType("html", "text/html");
        FIREFOX_45.registerUploadMimeType("htm", "text/html");
        FIREFOX_45.registerUploadMimeType("css", "text/css");
        FIREFOX_45.registerUploadMimeType("xml", "text/xml");
        FIREFOX_45.registerUploadMimeType("gif", "image/gif");
        FIREFOX_45.registerUploadMimeType("jpeg", "image/jpeg");
        FIREFOX_45.registerUploadMimeType("jpg", "image/jpeg");
        FIREFOX_45.registerUploadMimeType("mp4", "video/mp4");
        FIREFOX_45.registerUploadMimeType("m4v", "video/mp4");
        FIREFOX_45.registerUploadMimeType("m4a", "audio/mp4");
        FIREFOX_45.registerUploadMimeType("mp3", "audio/mpeg");
        FIREFOX_45.registerUploadMimeType("ogv", "video/ogg");
        FIREFOX_45.registerUploadMimeType("ogm", "video/x-ogm");
        FIREFOX_45.registerUploadMimeType("ogg", "video/ogg");
        FIREFOX_45.registerUploadMimeType("oga", "audio/ogg");
        FIREFOX_45.registerUploadMimeType("opus", "audio/ogg");
        FIREFOX_45.registerUploadMimeType("webm", "video/webm");
        FIREFOX_45.registerUploadMimeType("wav", "audio/wav");
        FIREFOX_45.registerUploadMimeType("flac", "audio/x-flac");
        FIREFOX_45.registerUploadMimeType("xhtml", "application/xhtml+xml");
        FIREFOX_45.registerUploadMimeType("xht", "application/xhtml+xml");
        FIREFOX_45.registerUploadMimeType("txt", "text/plain");
        FIREFOX_45.registerUploadMimeType("text", "text/plain");
        INTERNET_EXPLORER.registerUploadMimeType("html", "text/html");
        INTERNET_EXPLORER.registerUploadMimeType("htm", "text/html");
        INTERNET_EXPLORER.registerUploadMimeType("css", "text/css");
        INTERNET_EXPLORER.registerUploadMimeType("xml", "text/xml");
        INTERNET_EXPLORER.registerUploadMimeType("gif", "image/gif");
        INTERNET_EXPLORER.registerUploadMimeType("jpeg", "image/jpeg");
        INTERNET_EXPLORER.registerUploadMimeType("jpg", "image/jpeg");
        INTERNET_EXPLORER.registerUploadMimeType("mp4", "video/mp4");
        INTERNET_EXPLORER.registerUploadMimeType("m4v", "video/mp4");
        INTERNET_EXPLORER.registerUploadMimeType("m4a", "audio/mp4");
        INTERNET_EXPLORER.registerUploadMimeType("mp3", "audio/mpeg");
        INTERNET_EXPLORER.registerUploadMimeType("ogm", "video/x-ogm");
        INTERNET_EXPLORER.registerUploadMimeType("ogg", "application/ogg");
        INTERNET_EXPLORER.registerUploadMimeType("wav", "audio/wav");
        INTERNET_EXPLORER.registerUploadMimeType("xhtml", "application/xhtml+xml");
        INTERNET_EXPLORER.registerUploadMimeType("xht", "application/xhtml+xml");
        INTERNET_EXPLORER.registerUploadMimeType("txt", "text/plain");
        PluginConfiguration flash = new PluginConfiguration("Shockwave Flash", "Shockwave Flash 24.0 r0", "undefined", "internal-not-yet-present");
        flash.getMimeTypes().add(new MimeType("application/x-shockwave-flash", "Shockwave Flash", "swf"));
        CHROME.getPlugins().add(flash);
        flash = new PluginConfiguration("Shockwave Flash", "Shockwave Flash 24.0 r0", "24.0.0.194", "NPSWF32_24_0_0_194.dll");
        flash.getMimeTypes().add(new MimeType("application/x-shockwave-flash", "Shockwave Flash", "swf"));
        FIREFOX_45.getPlugins().add(flash);
        flash = new PluginConfiguration("Shockwave Flash", "Shockwave Flash 23.0 r0", "23.0.0.207", "Flash32_23_0_0_207.ocx");
        flash.getMimeTypes().add(new MimeType("application/x-shockwave-flash", "Shockwave Flash", "swf"));
        INTERNET_EXPLORER.getPlugins().add(flash);
        flash = new PluginConfiguration("Shockwave Flash", "Shockwave Flash 18.0 r0", "18.0.0.232", "Flash.ocx");
        flash.getMimeTypes().add(new MimeType("application/x-shockwave-flash", "Shockwave Flash", "swf"));
        EDGE.getPlugins().add(flash);
    }
}