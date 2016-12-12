package xj.property.event;

import android.view.View;

/**
 * Created by Administrator on 2015/6/9.
 */
public class ProviderEvaEvent {

    public ProviderEvaEvent(String from, String to, String fromNike, int cooperationId, String content) {
        this.from = from;
        this.to = to;
        this.fromNike = fromNike;
        this.cooperationId = cooperationId;
        this.content = content;
    }

    String from;
    String to;
    String fromNike;
    int cooperationId;
    String content;
    int circleLifeDetialId;
    View view;





    public ProviderEvaEvent(String from, String fromNike, int cooperationId, int circleLifeDetialId) {
        this.from = from;
        this.fromNike=fromNike;
        this.cooperationId = cooperationId;
        this.circleLifeDetialId = circleLifeDetialId;
    }

    public ProviderEvaEvent(String from, String to, int cooperationId, int circleLifeDetialId, String content) {
        this.from = from;
        this.to = to;
        this.cooperationId = cooperationId;
        this.circleLifeDetialId = circleLifeDetialId;
        this.content = content;
    }

    public ProviderEvaEvent(String from, String fromNike, int cooperationId, int circleLifeDetialId, View view) {
        this.from = from;
        this.fromNike = fromNike;
        this.cooperationId = cooperationId;
        this.circleLifeDetialId = circleLifeDetialId;
        this.view = view;
    }

    public String getFromNike() {
        return fromNike;
    }

    public void setFromNike(String fromNike) {
        this.fromNike = fromNike;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getCooperationId() {
        return cooperationId;
    }

    public void setCooperationId(int cooperationId) {
        this.cooperationId = cooperationId;
    }

    public int getCircleLifeDetialId() {
        return circleLifeDetialId;
    }

    public void setCircleLifeDetialId(int circleLifeDetialId) {
        this.circleLifeDetialId = circleLifeDetialId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
