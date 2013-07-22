package com.dcrux.buran.demoGui.plugins.fileApp.client;

import com.dcrux.buran.commandBase.VoidType;
import com.dcrux.buran.commands.incubation.CommitResult;
import com.dcrux.buran.common.IncNid;
import com.dcrux.buran.common.UserId;
import com.dcrux.buran.demoGui.plugins.listview.client.DescModule;
import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.ProgressBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.typedarrays.client.Int8ArrayNative;
import com.google.gwt.typedarrays.shared.ArrayBuffer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import org.vectomatic.dnd.DataTransferExt;
import org.vectomatic.dnd.DropPanel;
import org.vectomatic.file.Blob;
import org.vectomatic.file.File;
import org.vectomatic.file.FileList;
import org.vectomatic.file.FileReader;
import org.vectomatic.file.events.LoadEndEvent;
import org.vectomatic.file.events.LoadEndHandler;

/**
 * Buran.
 *
 * @author: ${USER} Date: 15.07.13 Time: 23:43
 */
public class FileUploadUi {
    interface FileMainUiUiBinder extends UiBinder<HTMLPanel, FileUploadUi> {
    }

    private FileModule fileModule = new FileModule(new UserId(0));
    private DescModule descModule = new DescModule(new UserId(0));

    private static class FileUploadInfo {
        private int fileIndex;
        private File file;
        private long start;
        private long end;
        private IncNid incNid;

        private int getFileIndex() {
            return fileIndex;
        }

        private void setFileIndex(int fileIndex) {
            this.fileIndex = fileIndex;
        }

        private File getFile() {
            return file;
        }

        private void setFile(File file) {
            this.file = file;
        }

        private long getStart() {
            return start;
        }

        private void setStart(long start) {
            this.start = start;
        }

        private long getEnd() {
            return end;
        }

        private void setEnd(long end) {
            this.end = end;
        }

        private IncNid getIncNid() {
            return incNid;
        }

        private void setIncNid(IncNid incNid) {
            this.incNid = incNid;
        }
    }

    private static class UploadInfo {
        private FileList fileList;
        private FileUploadInfo fileUploadInfo;
        private int fileIndex;
        private long combinedFileSize;
        private long completelyProcessedSize;

        private FileList getFileList() {
            return fileList;
        }

        private void setFileList(FileList fileList) {
            this.fileList = fileList;
        }

        private FileUploadInfo getFileUploadInfo() {
            return fileUploadInfo;
        }

        private void setFileUploadInfo(FileUploadInfo fileUploadInfo) {
            this.fileUploadInfo = fileUploadInfo;
        }

        private int getFileIndex() {
            return fileIndex;
        }

        private void setFileIndex(int fileIndex) {
            this.fileIndex = fileIndex;
        }

        private long getCombinedFileSize() {
            return combinedFileSize;
        }

        private void setCombinedFileSize(long combinedFileSize) {
            this.combinedFileSize = combinedFileSize;
        }

        private long getCompletelyProcessedSize() {
            return completelyProcessedSize;
        }

        private void setCompletelyProcessedSize(long completelyProcessedSize) {
            this.completelyProcessedSize = completelyProcessedSize;
        }
    }

    private HTMLPanel rootElement;

    private static final int UPLOAD_CHUNK = 8192 * 4;

    @UiField
    DropPanel dropPanel;
    @UiField
    HTMLPanel uploadInfoContainer;
    @UiField
    DivElement uploadInfoText;
    @UiField
    ProgressBar uploadInfoProgress;
    @UiField
    DivElement uploadOverallInfoText;
    @UiField
    ProgressBar uploadOverallInfoProgress;
    @UiField
    Alert statusField;

    private FileReader reader;

    private static FileMainUiUiBinder ourUiBinder = GWT.create(FileMainUiUiBinder.class);

    public FileUploadUi() {
        this.rootElement = ourUiBinder.createAndBindUi(this);
        showUploadDropinfo(true);
        this.reader = new FileReader();
        this.reader.addLoadEndHandler(this.loadHandler);
    }

    @UiHandler("dropPanel")
    public void handleDragEnter(DragEnterEvent event) {
        event.stopPropagation();
        event.preventDefault();
    }

    @UiHandler("dropPanel")
    public void handleDragLeave(DragLeaveEvent event) {
        event.stopPropagation();
        event.preventDefault();
    }

    @UiHandler("dropPanel")
    public void handleDragOver(DragOverEvent event) {
        event.stopPropagation();
        event.preventDefault();
    }

    @UiHandler("dropPanel")
    public void handleDrop(DropEvent event) {
        processFiles(event.getDataTransfer().<DataTransferExt>cast().getFiles(), 0);
        event.stopPropagation();
        event.preventDefault();
    }

    private static byte[] toByte(ArrayBuffer arrayBuffer) {
        Int8ArrayNative uint8Array = Int8ArrayNative.create(arrayBuffer, 0);
        final byte[] result = new byte[arrayBuffer.byteLength()];
        for (int i = 0; i < arrayBuffer.byteLength(); i++) {
            byte element = uint8Array.get(i);
            result[i] = (byte) (element);
        }
        return result;
    }

    private final LoadEndHandler loadHandler = new LoadEndHandler() {
        @Override
        public void onLoadEnd(LoadEndEvent loadEndEvent) {
            final IncNid incNid = uploadInfo.getFileUploadInfo().getIncNid();

            final byte[] data;
            data = toByte(FileUploadUi.this.reader.getArrayBufferResult());
            FileUploadUi.this.fileModule.append(incNid, data, new AsyncCallback<VoidType>() {
                @Override
                public void onFailure(Throwable caught) {
                    FileUploadUi.this.uploadInfoText.setInnerText("Error: " + caught);
                }

                @Override
                public void onSuccess(VoidType result) {
                    /* Increase start and end */
                    uploadInfo.getFileUploadInfo()
                            .setStart(uploadInfo.getFileUploadInfo().getEnd() + 1);
                    uploadInfo.getFileUploadInfo()
                            .setEnd(uploadInfo.getFileUploadInfo().getStart() + UPLOAD_CHUNK);
                    doNextUploadStep();
                }
            });

        }
    };

    private UploadInfo uploadInfo;

    private void showUploadDropinfo(boolean dropInfo) {
        this.uploadInfoContainer.setVisible(!dropInfo);
        this.dropPanel.setVisible(dropInfo);
    }

    private void setEndStatusText(String endStatusText) {
        this.statusField.setVisible(true);
        this.statusField.setText(endStatusText);
    }

    private void doNextUploadStep() {
        if (this.uploadInfo != null) {
            if (this.uploadInfo.getFileUploadInfo() == null) {
                /* First file? */
                if (this.uploadInfo.getFileIndex() == 0) {
                    showUploadDropinfo(false);
                }

                /* Begin new file */
                int fileIndex = this.uploadInfo.getFileIndex();
                if (fileIndex >= this.uploadInfo.getFileList().getLength()) {
                    /* Processed last file */
                    showUploadDropinfo(true);
                    setEndStatusText(this.uploadInfo.getFileList().getLength() + " Files " +
                            "uploaded.");
                }
                final FileUploadInfo fileUploadInfo = new FileUploadInfo();
                fileUploadInfo.setFileIndex(fileIndex);
                fileUploadInfo.setStart(0);
                fileUploadInfo.setEnd(UPLOAD_CHUNK);
                fileUploadInfo.setFile(this.uploadInfo.getFileList().getItem(fileIndex));
                this.uploadInfo.setFileUploadInfo(fileUploadInfo);

                /* Create node */
                String mime = fileUploadInfo.getFile().getType();
                if (mime == null) {
                    mime = "application/octet-stream";
                }
                this.fileModule.createFile(mime, new AsyncCallback<IncNid>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        FileUploadUi.this.uploadInfoText.setInnerText("Error: " + caught);
                    }

                    @Override
                    public void onSuccess(IncNid result) {
                        fileUploadInfo.setIncNid(result);
                        doNextUploadStep();
                    }
                });

            } else {
                /* Continue */
                final FileUploadInfo fileUploadInfo = this.uploadInfo.getFileUploadInfo();
                long fileSize = fileUploadInfo.getFile().getSize();

                if (fileUploadInfo.getEnd() >= fileSize) {
                    fileUploadInfo.setEnd(fileSize);
                }
                if (fileUploadInfo.getStart() >= fileUploadInfo.getEnd()) {
                    /* Commit file */
                    this.fileModule
                            .commit(fileUploadInfo.getIncNid(), fileUploadInfo.getFile().getName(),
                                    new AsyncCallback<CommitResult>() {

                                        @Override
                                        public void onFailure(Throwable caught) {
                                            FileUploadUi.this.uploadInfoText
                                                    .setInnerText("Error: " + caught);
                                        }

                                        @Override
                                        public void onSuccess(CommitResult result) {
                            /* Next file (or end) */
                                            FileUploadUi.this.uploadInfo.setFileIndex(
                                                    FileUploadUi.this.uploadInfo.getFileIndex() +
                                                            1);
                                            FileUploadUi.this.uploadInfo.setFileUploadInfo(null);
                                            doNextUploadStep();
                                        }
                                    });
                } else {
                    /* Display info */
                    int percentage =
                            (int) (((float) fileUploadInfo.getStart() / (float) fileSize) * 100f);
                    this.uploadInfoText.setInnerText("Uploading file " +
                            fileUploadInfo.getFile().getName() + "): " + percentage + "%");
                    this.uploadInfoProgress.setPercent(percentage);

                    /* Display overall info */
                    int overallPercentage =
                            (int) ((float) this.uploadInfo.getCompletelyProcessedSize() /
                                    (float) this.uploadInfo.getCombinedFileSize() * 100f);
                    this.uploadOverallInfoText.setInnerText("Overall progress: Uploading file" +
                            " " +
                            (fileUploadInfo.getFileIndex() + 1) + " of " +
                            this.uploadInfo.getFileList().getLength() + ": " +
                            overallPercentage + "%");
                    this.uploadOverallInfoProgress.setPercent(overallPercentage);

                    /* Read from file */
                    Blob blob = fileUploadInfo.getFile()
                            .slice(fileUploadInfo.getStart(), fileUploadInfo.getEnd());
                    this.reader.readAsArrayBuffer(blob);
                     /* Processed size */
                    this.uploadInfo.setCompletelyProcessedSize(
                            this.uploadInfo.getCompletelyProcessedSize() +
                                    (fileUploadInfo.getEnd() - fileUploadInfo.getStart()));
                }
            }
        }
    }

    private void processFiles(FileList files, int startIndex) {
        if (startIndex >= files.getLength()) {
            return;
        }

        this.uploadInfo = new UploadInfo();
        this.uploadInfo.setFileList(files);
        this.uploadInfo.setFileIndex(0);
        long combinedSize = 0;
        for (int i = 0; i < files.getLength(); i++) {
            final File file = files.getItem(i);
            combinedSize += file.getSize();
        }
        this.uploadInfo.setCombinedFileSize(combinedSize);
        doNextUploadStep();
    }

    public HTMLPanel getRootElement() {
        return rootElement;
    }
}