/*
   Copyright Jaimon Mathew <mail@jaimon.co.uk>

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package jaimon;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletOutputStream;

public class ResourceIO {

    public static void writeResource(ServletOutputStream out, String filename) throws IOException {
        InputStream is = ResourceIO.class.getClassLoader().getResourceAsStream("resources/" + filename);
        if(is == null) {
            throw new FileNotFoundException(filename);
        }
        BufferedInputStream bis = new BufferedInputStream(is);
        byte input[] = new byte[2048];
        boolean eof = false;
        while (!eof) {
            int length = bis.read(input);
            if (length == -1)
                eof = true;
            else
                out.write(input, 0, length);
        }
        is.close();
    }
}

