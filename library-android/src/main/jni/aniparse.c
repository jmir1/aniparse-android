/**
    This file defines the JNI implementation of the PyBridge class.
    It implements the native methods of the class and makes sure that
    all the prints and errors from the Python interpreter is redirected
    to the Android log. This is specially useful as it allows us to
    debug the Python code running on the Android device using logcat.
*/

#include <Python.h>
#include <jni.h>

/* ------------------ */
/*  Global variables  */
/* ------------------ */

PyObject *main_module;
PyObject *global_dict;
PyObject *local_dict;

/* ------------------ */
/*   Native methods   */
/* ------------------ */

/**
 * This function configures the location of the standard library and
 * initializes the interpreter.
 * Note: the function must receive a string with the location of the
 * python files extracted from the assets folder.
*/
JNIEXPORT jint JNICALL
Java_com_github_jmir1_aniparseandroid_library_Parser_00024Companion_startExternal(JNIEnv *env,
                                                                                          jobject thiz,
                                                                                          jstring path)
{
    // Get the location of the python files
    const char *pypath = (*env)->GetStringUTFChars(env, path, NULL);

    // Build paths for the Python interpreter
    char paths[512];
    snprintf(paths, sizeof(paths), "%s:%s/stdlib.zip:%s/modules:%s/site-packages", pypath, pypath, pypath, pypath);

    // Set Python paths
    wchar_t *wchar_paths = Py_DecodeLocale(pypath, NULL);
    Py_SetPath(wchar_paths);

    // Initialize Python interpreter and logging
    Py_InitializeEx(0);

    // Import modules
    PyRun_SimpleString("import aniparse");

    // Initialize dicts for evaluating stuff
    main_module = PyImport_AddModule("__main__");
    global_dict = PyModule_GetDict(main_module);
    local_dict = PyDict_New();

    // Cleanup
    (*env)->ReleaseStringUTFChars(env, path, pypath);
    PyMem_RawFree(wchar_paths);

    return 0;
}

/**
 * This function stops the python interpreter and cleans up everything.
 */
JNIEXPORT jint JNICALL
Java_com_github_jmir1_aniparseandroid_library_Parser_00024Companion_stopExternal(JNIEnv *env,
                                                                                         jobject thiz)
{
    Py_DECREF(local_dict);
    Py_DECREF(global_dict);
    Py_DECREF(main_module);
    Py_Finalize();
    return 0;
}


/**
 * This function evaluates the given payload string and returns the
 * result as a string representation or null if there is an error.
 */
JNIEXPORT jstring JNICALL
Java_com_github_jmir1_aniparseandroid_library_Parser_00024Companion_call(JNIEnv *env,
                                                                                 jobject thiz,
                                                                                 jstring payload)
{
    // Variables
    PyObject *codeObject;
    PyObject *resultObject;
    PyObject *resultPythonString;
    const char *resultString;
    jstring result;

    // Get the payload string
    jboolean iscopy;
    const char *payload_utf = (*env)->GetStringUTFChars(env, payload, &iscopy);

    // Compile the payload and evaluate it
    codeObject = Py_CompileString(payload_utf, "", Py_eval_input);
    if (codeObject)
        resultObject = PyEval_EvalCode(codeObject, global_dict, local_dict);
    if (resultObject)
        resultPythonString = PyObject_Str(resultObject);
    if (resultPythonString)
        resultString = PyUnicode_AsUTF8(resultPythonString);

    // Store the result on a java.lang.String object
    result = (*env)->NewStringUTF(env, resultString);

    // Cleanup
    (*env)->ReleaseStringUTFChars(env, payload, payload_utf);
    Py_XDECREF(codeObject);
    Py_XDECREF(resultObject);
    Py_XDECREF(resultPythonString);

    return result;
}
