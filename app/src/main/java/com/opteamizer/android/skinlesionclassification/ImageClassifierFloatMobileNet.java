package com.opteamizer.android.skinlesionclassification;

import android.app.Activity;
import java.io.IOException;

public class ImageClassifierFloatMobileNet extends ImageClassifier {

  private float[][] labelProbArray = null;

  ImageClassifierFloatMobileNet(Activity activity) throws IOException {
    super(activity);
    labelProbArray = new float[1][getNumLabels()];
  }

  @Override
  protected String getModelPath() {
    return "model.tflite";
  }

  @Override
  protected String getLabelPath() { return "labels.txt";
  }

  @Override
  protected int getImageSizeX() {
    return 224;
  }

  @Override
  protected int getImageSizeY() {
    return 224;
  }

  @Override
  protected int getNumBytesPerChannel() {
    return 4; // Float.SIZE / Byte.SIZE;
  }

  @Override
  protected void addPixelValue(int pixelValue) {
      imgData.putFloat(((((pixelValue >> 16) & 0xFF)*2.f) / 255.f)-1.f);
      imgData.putFloat(((((pixelValue >> 8) & 0xFF)*2.f) / 255.f)-1.f);
      imgData.putFloat((((pixelValue & 0xFF)*2.f) / 255.f)-1.f);
    }

  @Override
  protected float getProbability(int labelIndex) {
    return labelProbArray[0][labelIndex];
  }

  @Override
  protected void setProbability(int labelIndex, Number value) {
    labelProbArray[0][labelIndex] = value.floatValue();
  }

  @Override
  protected float getNormalizedProbability(int labelIndex) {
    return labelProbArray[0][labelIndex];
  }

  @Override
  protected void runInference() {
    tflite.run(imgData, labelProbArray);
  }
}
