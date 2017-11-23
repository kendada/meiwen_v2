package cc.meiwen.model;

import java.util.List;

/**
 * Created by abc on 2017/11/22.
 */

public class FriedsTimelineBO {

    public List<FriendsTimelineStatusesBO> statuses;

    public long next_cursor;

    public int total_number;

    public List<FriendsTimelineStatusesBO> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<FriendsTimelineStatusesBO> statuses) {
        this.statuses = statuses;
    }

    public long getNext_cursor() {
        return next_cursor;
    }

    public void setNext_cursor(long next_cursor) {
        this.next_cursor = next_cursor;
    }

    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }

    @Override
    public String toString() {
        return "FriedsTimelineBO{" +
                "statuses=" + statuses +
                ", next_cursor=" + next_cursor +
                ", total_number=" + total_number +
                '}';
    }
}
