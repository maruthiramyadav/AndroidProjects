package omgbuzz.devobl.com.testprojectone.VideoView.subtitle;

import java.io.File;

public interface DownloadCallback {
    public void onDownload(File file);
    public void onFail(Exception e);
}
