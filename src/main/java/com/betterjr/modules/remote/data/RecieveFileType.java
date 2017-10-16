package com.betterjr.modules.remote.data;

import org.apache.commons.lang3.StringUtils;

import com.betterjr.common.utils.BetterStringUtils;

public enum RecieveFileType {
    TEXT(".txt"), GZIP(".gz"), ZIP(".zip");

    private final String fileType;

    RecieveFileType(String anType) {
        this.fileType = anType;
    }

    public String getFileType() {
        return this.fileType;
    }

    public static RecieveFileType checking(String anWorkType) {
        try {
            if (StringUtils.isNotBlank(anWorkType)) {

                return RecieveFileType.valueOf(anWorkType.trim().toUpperCase());
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return RecieveFileType.TEXT;
    }

    /**
     * 将文件的扩展名转换为我们认可的扩展名。
     * @param anFileName
     * @return
     */
    public String findFileName(String anFileName) {
        if (StringUtils.isNotBlank(anFileName)) {
            if (anFileName.toLowerCase().endsWith(this.fileType)) {
                return anFileName;
            }
        }
        return anFileName.concat(fileType);
    }
}
