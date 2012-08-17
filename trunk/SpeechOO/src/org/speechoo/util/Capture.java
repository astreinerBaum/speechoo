/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.speechoo.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/**
 *
 * @author Hugo Santos
 */
public class Capture implements Runnable {

    TargetDataLine line;
    Thread thread;
    AudioInputStream audioInputStream;
    double duration;
    File file;

    public void start(File file) {
        this.file = file;

        thread = new Thread(this);
        thread.setName("Capture");
        thread.start();
    }

    public void stop() {
        thread = null;

    }

    private void shutdown(String message) {
        if (thread != null) {
            thread = null;
            System.err.println(message);
        }
    }

    public void run() {

        duration = 0;
        audioInputStream = null;

        // define the required attributes for our line,
        // and make sure a compatible line is supported.

        if (file != null) {
            createAudioInputStream(file);
        }

        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
        float rate = 16000;
        int sampleSize = 16;
        int channels = 1;
        boolean bigEndian = false;

        AudioFormat format = new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);

        DataLine.Info info = new DataLine.Info(TargetDataLine.class,
                format);

        if (!AudioSystem.isLineSupported(info)) {
            shutdown("Line matching " + info + " not supported.");
            return;
        }

        // get and open the target data line for capture.

        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format, line.getBufferSize());
        } catch (LineUnavailableException ex) {
            shutdown("Unable to open the line: " + ex);
            ex.printStackTrace();
            return;
        } catch (SecurityException ex) {
            shutdown(ex.toString());
            ex.printStackTrace();
            return;
        } catch (Exception ex) {
            shutdown(ex.toString());
            ex.printStackTrace();
            return;
        }
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int frameSizeInBytes = format.getFrameSize();
        int bufferLengthInFrames = line.getBufferSize() / 8;
        int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
        byte[] data = new byte[bufferLengthInBytes];
        int numBytesRead;

        line.start();

        while (thread != null) {
            if ((numBytesRead = line.read(data, 0, bufferLengthInBytes)) == -1) {
                break;
            }
            out.write(data, 0, numBytesRead);
        }

        // we reached the end of the stream.  stop and close the line.
        line.stop();
        line.close();
        line = null;

        // stop and close the output stream
        try {
            out.flush();
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // load bytes into the audio input stream to save on file

        byte audioBytes[] = out.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
        audioInputStream = new AudioInputStream(bais, format, audioBytes.length / frameSizeInBytes);

        long milliseconds = (long) ((audioInputStream.getFrameLength() * 1000) / format.getFrameRate());
        duration = milliseconds / 1000.0;

        saveToFile();

        try {
            audioInputStream.reset();
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
    }

    private void createAudioInputStream(File file) {
        if (file != null && file.isFile()) {
            try {
                this.file = file;
                System.out.println("Capture File: " + this.file.getAbsolutePath());
                audioInputStream = AudioSystem.getAudioInputStream(file);
                long milliseconds = (long) ((audioInputStream.getFrameLength() * 1000) / audioInputStream.getFormat().getFrameRate());
                duration = milliseconds / 1000.0;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void saveToFile() {

        AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

        if (audioInputStream == null) {
            System.out.println("No loaded audio to save");
            return;
        } else if (file != null) {
            createAudioInputStream(file);
        }

        // reset to the beginnning of the captured data
        try {
            audioInputStream.reset();
        } catch (Exception e) {
            System.out.println("Unable to reset stream " + e);
            return;
        }

        try {
            if (AudioSystem.write(audioInputStream, fileType, file) == -1) {
                throw new IOException("Problems writing to file");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
