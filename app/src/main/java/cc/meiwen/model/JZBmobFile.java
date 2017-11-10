package cc.meiwen.model;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by abc on 2017/11/10.
 */

public class JZBmobFile extends BmobFile{

    public JZBmobFile(File file) {
        super(file);
    }

    @Override
    public void setFilename(String filename) {
        super.setFilename(filename);
    }
}
