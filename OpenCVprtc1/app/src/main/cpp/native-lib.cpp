#include <jni.h>
#include <opencv2/opencv.hpp>

using namespace cv;

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_opencvprtc1_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_opencvprtc1_MainActivity_ConvertRGBtoGray(JNIEnv *env, jobject thiz,
                                                           jlong mat_addr_input,
                                                           jlong mat_addr_result) {
    // TODO: implement ConvertRGBtoGray()
    Mat &matInput = *(Mat *)mat_addr_input;
    Mat &matResult = *(Mat *)mat_addr_result;

    cvtColor(matInput, matResult, COLOR_RGB2GRAY);
}