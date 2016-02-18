package cc.meiwen.util.task;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-29
 * Time: 14:14
 * Version 1.0
 */

public abstract class ThreadPoolTask implements Runnable {

    public abstract void updata(Object obj);  //更新数据

    public abstract Object loadData();  //加载数据

}
