package com.skripsi.stegano;

import android.annotation.SuppressLint;

import java.io.*;
//import java.util.Scanner;
import java.util.zip.*;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class steganografi {
    public static final String VERSION = "2.0.0";
    public static final byte[] VERSION_BYTE = {'3', '4', '5'};
    public static final int OFFSET = 16;
    public static final byte CEF = 1;
    private static Cipher cipher;
    private static SecretKeySpec spec;
    private static String masterExtension, message, image;
    private static File masterFile, outputFile;
    private static byte features;
    private static int inputFileSize;
    private static int i, j, inputOutputMarker, messageSize, tempInt;
    private static short compressionRatio = 0;
    private static byte byte1, byte2, byte3, byteArrayIn[];
    private static ByteArrayOutputStream byteOut;


    public steganografi() {
        System.out.println("steganografi " + VERSION + " siap...");
    }

    public static String getMessage() {
        return message;
    }

    public static String getImage() {
        return image;
    }

    public static File getFile() { return outputFile; }

    // Encodes a file into a Master file
    @SuppressLint("SdCardPath")
    public static boolean encodeFile(File masterFile, File savefile, File dataFile, int compression) {

        String saveFile = savefile.getName().toString();

        messageSize = (int) dataFile.length();

        features = CEF;

        inputFileSize = (int) masterFile.length();

        try {

            byteOut = new ByteArrayOutputStream();
            byteArrayIn = new byte[inputFileSize];
            DataInputStream in = new DataInputStream(new FileInputStream(masterFile));
            in.read(byteArrayIn, 0, inputFileSize);
            in.close();
            String fileName = masterFile.getName();

            //mencari extensi file master
            masterExtension = fileName.substring(fileName.length() - 3, fileName.length());

            // Skip past OFFSET_GIF_BMP_TIF bytes
            byteOut.write(byteArrayIn, 0, OFFSET);
            inputOutputMarker = OFFSET;

            // shif bitwise
            byte tempByte[] = new byte[4];
            for (i = 24, j = 0; i >= 0; i -= 8, j++) {
                tempInt = inputFileSize;
                tempInt >>= i;
                tempInt &= 0x000000FF; // nilai 255
                tempByte[j] = (byte) tempInt;
            }

            // Encode 4 byte input File size array into the master file
            encodeBytes(tempByte);
            // Write the remaining bytes
            byteOut.write(byteArrayIn, inputOutputMarker, inputFileSize - inputOutputMarker);
            inputOutputMarker = inputFileSize;
            //System.out.print(byteOut);
            // Encode the 3 byte version information into the file
            writeBytes(VERSION_BYTE);
            // Write 1 byte for features
            writeBytes(new byte[]{features});
            // Read the data bytes into fileArray
            byte[] fileArray = new byte[messageSize];
            in = new DataInputStream(new FileInputStream(dataFile));
            in.read(fileArray, 0, messageSize);
            in.close();
            //Compress the message if required
//           if (features == CEF) {
//                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
//                ZipOutputStream zOut = new ZipOutputStream(arrayOutputStream);
//                ZipEntry entry = new ZipEntry(dataFile.getName());
//                zOut.setLevel(compression);
//                zOut.putNextEntry(entry);
//                zOut.write(fileArray, 0, messageSize);
//                zOut.closeEntry();
//                zOut.finish();
//                zOut.close();
//                // Get the compressed message byte array
//                fileArray = arrayOutputStream.toByteArray();
//                compressionRatio = (short) ((double) fileArray.length / (double) messageSize * 100.0);
//               messageSize = fileArray.length;
//            }
            // Encode 1 byte compression ratio into the output file
//            writeBytes(new byte[]{(byte) compressionRatio});
//            StringBuffer fileContents = new StringBuffer();
            messageSize = fileArray.length;

            tempByte = new byte[4];
            for (i = 24, j = 0; i >= 0; i -= 8, j++) {
                tempInt = messageSize;
                tempInt >>= i;

                tempInt &= 0x000000FF;
                tempByte[j] = (byte) tempInt;
                //System.out.print(tempByte[j]);
            }
            writeBytes(tempByte);
            // Encode the message
            writeBytes(fileArray);

            //simpan 
            File f = new File("/sdcard/Encode/" + saveFile);
            File dir = new File("/sdcard/Encode");
            if (!dir.exists()) {
                dir.mkdir();
            }
            f.createNewFile();
            DataOutputStream out = new DataOutputStream(new FileOutputStream(f));
            byteOut.writeTo(out);

            out.close();


        } catch (EOFException e) {
            message = e.getMessage().toString();
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            message = "Oops!!\nError: " + e.getMessage().toString();

            e.printStackTrace();
            return false;
        }

        message = "File '" + dataFile.getName() + "' Encode successfully in file '" + saveFile;
        return false;
    }

    // Decodes an encodeded file from a Master file
    @SuppressLint("SdCardPath")
    public static boolean decodeFile(SteganoInformation info, boolean overwrite) {
//    	String key ="vigen";
        File dataFile = null;
        features = info.getFeatures();

        try {
            masterFile = info.getFile();

            byteArrayIn = new byte[(int) masterFile.length()];

            DataInputStream in = new DataInputStream(new FileInputStream(masterFile));
            in.read(byteArrayIn, 0, (int) masterFile.length());
            in.close();

            messageSize = info.getDataLength();

            byte[] fileArray = new byte[(int) messageSize];
            inputOutputMarker = info.getInputMarker();
            readBytes(fileArray);


            if (messageSize <= 0) {
                message = "Unexpected size of Encode file: 0.";
                return false;
            }
            //Decrypt the file if required

            // Uncompress the file if required
            if (features == CEF) {
                ByteArrayOutputStream by = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(by);

                ZipInputStream zipIn = new ZipInputStream(new ByteArrayInputStream(fileArray));
                ZipEntry entry = zipIn.getNextEntry();
                dataFile = new File(entry.getName());
                byteArrayIn = new byte[1024];
                while ((tempInt = zipIn.read(byteArrayIn, 0, 1024)) != -1) {
                    out.write(byteArrayIn, 0, tempInt);
                }
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

            // save file dokumen
            File f = new File("/sdcard/Decode/" + dataFile.toString());
            File dir = new File("/sdcard/Decode");
            if (!dir.exists()) {
                dir.mkdir();
            }
            f.createNewFile();
            //fungsi membaca file dan di save
            DataOutputStream out = new DataOutputStream(new FileOutputStream(f));
            out.write(fileArray, 0, fileArray.length);
            out.close();
            outputFile = f;
        } catch (Exception e) {
            message = "Oops!!\n Error: " + e;
//            Toast.makeText(conten, message, Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return false;
        }



//        message = "Decode file " + dataFile.toString() + " Successfuly";
//        Toast.makeText(conten, message, Toast.LENGTH_LONG).show();
        return true;
    }

    private static void encodeBytes(byte[] bytes) {
        int size = bytes.length;
        for (int i = 0; i < size; i++) {
            byte1 = bytes[i];
            for (int j = 6; j >= 0; j -= 2) {
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

    // Method used to write bytes into the output array
    private static void writeBytes(byte[] bytes) {
        int size = bytes.length;

        for (int i = 0; i < size; i++) {
            byteOut.write(bytes[i]);
            inputOutputMarker++;
        }
        //System.out.println("\n"+byteOut);
    }

    // Method used to decode bytes into the output array
    @SuppressWarnings("unused")
    private static void decodeBytes(byte[] bytes) {
        int size = bytes.length;

        for (int i = 0; i < size; i++) {
            byte1 = 0;
            for (int j = 6; j >= 0; j -= 2) {
                byte2 = byteArrayIn[inputOutputMarker];
                inputOutputMarker++;

                byte2 &= 0x03;
                byte2 <<= j;
                byte1 |= byte2;
            }
            bytes[i] = byte1;
        }
    }

    // Method used to read bytes into the output array
    private static void readBytes(byte[] bytes) {
        int size = bytes.length;
        for (int i = 0; i < size; i++) {
            bytes[i] = byteArrayIn[inputOutputMarker];
            inputOutputMarker++;
        }
    }
}
