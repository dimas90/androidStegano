package com.skripsi.stegano;

import java.io.*;
import java.util.zip.*;

import javax.crypto.*;
import javax.crypto.spec.*;


public class VideoSteganography {
    public static final int OFFSET_JPG = 3;
    public static final int OFFSET_PNG = 42;
    public static final int OFFSET_GIF_BMP_TIF = 32;
    public static final short HEADER_LENGTH = 15 * 4;
    public static final byte[] VERSION_BYTE = {'2', '0', '0'};
    public static final byte UUF = 1;
    public static final byte UEF = 2;
    public static final byte CUF = 3;
    public static final byte CEF = 4;

    private static Cipher cipher;

    private static SecretKeySpec spec;
    private static String masterExtension, message;

    private static File masterFile;

    private static byte features;
    private static int inputFileSize;
    private static int i, j, inputOutputMarker, messageSize, tempInt;
    private static short compressionRatio = 0, temp;
    private static byte byte1, byte2, byte3, byteArrayIn[];
    private static ByteArrayOutputStream byteOut;

    private VideoSteganography() {
        System.out.println("Steganography  ready...");
    }

    public static String getMessage() {
        return message;
    }


    public static boolean embedFile(File masterFile, File outputFile, File dataFile, int compression, String password) {
        messageSize = (int) dataFile.length();

        if (password != null && password.length() < 16) {
            message = "Password should be minimum of 16 Characters";
            return false;
        }

        if (compression != 0) {

            if (compression < 0) compression = 0;
            if (compression > 9) compression = 9;

            if (password == null) features = CUF;
            else features = CEF;
        } else {
            if (password == null) features = UUF;
            else features = UEF;
        }

        inputFileSize = (int) masterFile.length();
        try {

            byteOut = new ByteArrayOutputStream();

// create a byte array of length equal to size of input file
            byteArrayIn = new byte[inputFileSize];

            // Open the input file read all bytes into byteArrayIn
            DataInputStream in = new DataInputStream(new FileInputStream(masterFile));
            in.read(byteArrayIn, 0, inputFileSize);
            in.close();

            String fileName = masterFile.getName();
            masterExtension = fileName.substring(fileName.length() - 3, fileName.length());

            if (masterExtension.equalsIgnoreCase("jpg")) {
                // Skip past OFFSET_JPG bytes
                byteOut.write(byteArrayIn, 0, OFFSET_JPG);
                inputOutputMarker = OFFSET_JPG;
            } else if (masterExtension.equalsIgnoreCase("png")) {

                byteOut.write(byteArrayIn, 0, OFFSET_PNG);
                inputOutputMarker = OFFSET_PNG;
            } else {

                byteOut.write(byteArrayIn, 0, OFFSET_GIF_BMP_TIF);
                inputOutputMarker = OFFSET_GIF_BMP_TIF;
            }


            // Convert the 32 bit input file size into byte array
            byte tempByte[] = new byte[4];
            for (i = 24, j = 0; i >= 0; i -= 8, j++) {
                tempInt = inputFileSize;
                tempInt >>= i;
                tempInt &= 0x000000FF;
                tempByte[j] = (byte) tempInt;
            }

            // Embed 4 byte input File size array into the master file

            embedBytes(tempByte);


            byteOut.write(byteArrayIn, inputOutputMarker, inputFileSize - inputOutputMarker);
            inputOutputMarker = inputFileSize;


            writeBytes(VERSION_BYTE);
            writeBytes(new byte[]{features});

            // Read the data bytes into fileArray
            byte[] fileArray = new byte[messageSize];
            in = new DataInputStream(new FileInputStream(dataFile));
            in.read(fileArray, 0, messageSize);
            in.close();
//
//            if (features == CUF || features == CEF) {
//                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
//                ZipOutputStream zOut = new ZipOutputStream(arrayOutputStream);
//                ZipEntry entry = new ZipEntry(dataFile.getName());
//                zOut.setLevel(compression);
//                zOut.putNextEntry(entry);
//                zOut.write(fileArray, 0, messageSize);
//                zOut.closeEntry();
//                zOut.finish();
//                zOut.close();
//
//                // Get the compressed message byte array
//                fileArray = arrayOutputStream.toByteArray();
//                compressionRatio = (short) ((double) fileArray.length / (double) messageSize * 100.0);
//                messageSize = fileArray.length;
//            }
//
//            // Embed 1 byte compression ratio into the output file
//            writeBytes(new byte[]{(byte) compressionRatio});

//
//            if (features == UEF || features == CEF) {
//                Cipher cipher = Cipher.getInstance("AES");
//                SecretKeySpec spec = new SecretKeySpec(password.substring(0, 16).getBytes(), "AES");
//                cipher.init(Cipher.ENCRYPT_MODE, spec);
//                fileArray = cipher.doFinal(fileArray);
//                messageSize = fileArray.length;
//            }

            tempByte = new byte[4];
            for (i = 24, j = 0; i >= 0; i -= 8, j++) {
                tempInt = messageSize;
                tempInt >>= i;
                tempInt &= 0x000000FF;
                tempByte[j] = (byte) tempInt;
            }

            writeBytes(tempByte);

            // Embed the message
            writeBytes(fileArray);

            File f = new File("/sdcard/Encode/Test."+masterExtension);
            File dir = new File("/sdcard/Encode");
            if (!dir.exists()){
                dir.mkdir();
            }
            f.createNewFile();

            DataOutputStream out = new DataOutputStream(new FileOutputStream(f));
            byteOut.writeTo(out);
            out.close();
        } catch (EOFException e) {
        } catch (Exception e) {
            message = "Oops!!\nError: " + e.toString();
            e.printStackTrace();
            return false;
        }

        message = "File '" + dataFile.getName() + "' embedded successfully in file '" + outputFile.getName() + "'.";
        return true;
    }

    // Retrieves an embedded file from a Master file
    public static boolean retrieveFile(SteganoInformation info, String password, boolean overwrite) {
        File dataFile = null;
        features = info.getFeatures();

        try {
            masterFile = info.getFile();
            byteArrayIn = new byte[(int) masterFile.length()];

            DataInputStream in = new DataInputStream(new FileInputStream(masterFile));
            in.read(byteArrayIn, 0, (int) masterFile.length());
            in.close();

            messageSize = info.getDataLength();
            byte[] fileArray = new byte[messageSize];
            inputOutputMarker = info.getInputMarker();
            readBytes(fileArray);

            if (messageSize <= 0) {
                message = "Unexpected size of embedded file: 0.";
                return false;
            }


            if (features == CEF || features == UEF) {
                password = password.substring(0, 16);
                byte passwordBytes[] = password.getBytes();
                cipher = Cipher.getInstance("AES");
                spec = new SecretKeySpec(passwordBytes, "AES");
                cipher.init(Cipher.DECRYPT_MODE, spec);
                try {
                    fileArray = cipher.doFinal(fileArray);
                } catch (Exception bp) {
                    message = "Incorrent Password";
                    bp.printStackTrace();
                    return false;
                }
                messageSize = fileArray.length;
            }


            if (features == CUF || features == CEF) {
                ByteArrayOutputStream by = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(by);

                ZipInputStream zipIn = new ZipInputStream(new ByteArrayInputStream(fileArray));
                ZipEntry entry = zipIn.getNextEntry();
                dataFile = new File(entry.getName());

                byteArrayIn = new byte[1024];
                while ((tempInt = zipIn.read(byteArrayIn, 0, 1024)) != -1)
                    out.write(byteArrayIn, 0, tempInt);

                zipIn.close();
                out.close();
                fileArray = by.toByteArray();
                messageSize = fileArray.length;
            }

            info.setDataFile(dataFile);
            if (dataFile.exists() && !overwrite) {
                message = "File Exists";
                return false;
            }

            DataOutputStream out = new DataOutputStream(new FileOutputStream(dataFile));
            out.write(fileArray, 0, fileArray.length);
            out.close();
        } catch (Exception e) {
            message = "Oops!!\n Error: " + e;
            e.printStackTrace();
            return false;
        }

        message = "Retrieved file size: " + messageSize + " B";
        return true;
    }


    private static void embedBytes(byte[] bytes) {
        int size = bytes.length;

        for (int i = 0; i < size; i++) {
            byte1 = bytes[i];
            for (int j = 8; j >= 0; j -= 2) {
                byte2 = byte1;
                byte2 >>= j;
                byte2 &= 0x03;

                byte3 = byteArrayIn[inputOutputMarker];
                byte3 &= 0xFC;
                byte3 |= byte2;
                byteOut.write(byte3);
                inputOutputMarker++;
            }
        }
    }


    private static void writeBytes(byte[] bytes) {
        int size = bytes.length;

        for (int i = 0; i < size; i++) {
            byteOut.write(bytes[i]);
            inputOutputMarker++;
        }
    }


    private static void retrieveBytes(byte[] bytes) {
        int size = bytes.length;

        for (int i = 0; i < size; i++) {
            byte1 = 0;
            for (int j = 8; j >= 0; j -= 2) {
                byte2 = byteArrayIn[inputOutputMarker];
                inputOutputMarker++;

                byte2 &= 0x03;
                byte2 <<= j;
                byte1 |= byte2;
            }
            bytes[i] = byte1;
        }
    }


    private static void readBytes(byte[] bytes) {
        int size = bytes.length;

        for (int i = 0; i < size; i++) {
            bytes[i] = byteArrayIn[inputOutputMarker];
            inputOutputMarker++;
        }
    }


}

