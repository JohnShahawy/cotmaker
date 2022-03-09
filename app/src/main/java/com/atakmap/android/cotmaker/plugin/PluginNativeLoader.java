/*
MIT License

Copyright (c) 2022 Nic Cellular

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.atakmap.android.cotmaker.plugin;

import java.io.File;
import android.content.Context;

/**
 * Boilerplate code for loading native.
 */
public class PluginNativeLoader {

    private static final String TAG = "NativeLoader";
    private static String ndl = null;

    /**
    * If a plugin wishes to make use of this class, they will need to copy it into their plugin.
    * The classloader that loads this class is a key component of getting System.load to work 
    * properly.   If it is desirable to use this in a plugin, it will need to be a direct copy in a
    * non-conflicting package name.
    */
    synchronized static public void init(final Context context) {
        if (ndl == null) {
            try {
                ndl = context.getPackageManager()
                        .getApplicationInfo(context.getPackageName(),
                                0).nativeLibraryDir;
            } catch (Exception e) {
                throw new IllegalArgumentException(
                        "native library loading will fail, unable to grab the nativeLibraryDir from the package name");
            }

        }
    }

    /**
    * Security guidance from our recent audit:
    * Pass an absolute path to System.load(). Avoid System.loadLibrary() because its behavior 
    * depends upon its implementation which often relies on environmental features that can be 
    * manipulated. Use only validated, sanitized absolute paths.
    */

    public static void loadLibrary(final String name) {
        if (ndl != null) {
            final String lib = ndl + File.separator
                    + System.mapLibraryName(name);
            if (new File(lib).exists()) {
                System.load(lib);
            }
        } else {
            throw new IllegalArgumentException("NativeLoader not initialized");
        }

    }

}
