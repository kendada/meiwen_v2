package cc.meiwen.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-27
 * Time: 16:39
 * Version 1.0
 */

public class Datas implements Parcelable {

    private List<Post> list;

    private List<PostType> typeList;

    private List<Favo> favoList;

    private List<Artcile> artcileList;

    public Datas(List<Post> list) {
        this.list = list;
    }

    public Datas(List<PostType> typeList, String type) {
        this.typeList = typeList;
    }

    public Datas(List<Favo> favoList, int favo) {
        this.favoList = favoList;
    }

    public Datas(List<Artcile> artcileList, float v){
        this.artcileList = artcileList;
    }

    public Datas() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.list);
        dest.writeList(this.typeList);
        dest.writeList(this.favoList);
        dest.writeList(this.artcileList);
    }

    protected Datas(Parcel in) {
        this.list = new ArrayList<Post>();
        in.readList(this.list, List.class.getClassLoader());
        this.typeList = new ArrayList<PostType>();
        in.readList(this.typeList, List.class.getClassLoader());
        this.favoList = new ArrayList<Favo>();
        in.readList(this.favoList, List.class.getClassLoader());
        this.artcileList = new ArrayList<Artcile>();
        in.readList(this.artcileList, List.class.getClassLoader());
    }

    public static final Creator<Datas> CREATOR = new Creator<Datas>() {
        public Datas createFromParcel(Parcel source) {
            return new Datas(source);
        }

        public Datas[] newArray(int size) {
            return new Datas[size];
        }
    };

    public List<Post> getList() {
        return list;
    }

    public void setList(List<Post> list) {
        this.list = list;
    }

    public List<PostType> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<PostType> typeList) {
        this.typeList = typeList;
    }

    public List<Favo> getFavoList() {
        return favoList;
    }

    public void setFavoList(List<Favo> favoList) {
        this.favoList = favoList;
    }

    public List<Artcile> getArtcileList() {
        return artcileList;
    }

    public void setArtcileList(List<Artcile> artcileList) {
        this.artcileList = artcileList;
    }
}
