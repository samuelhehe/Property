package xj.property.event;

import android.view.View;

/**
 * Created by Administrator on 2015/6/9.
 */
public class EvaEvent {
    String from;
    String to;
    String fromNike;
    int circleLifeId;
    int circleLifeDetialId;
    String content;
    View view;

    public EvaEvent( String from, String fromNike,int circleLifeId, int circleLifeDetialId) {
        this.from = from;
        this.fromNike=fromNike;
        this.circleLifeId = circleLifeId;
        this.circleLifeDetialId = circleLifeDetialId;
    }

    public EvaEvent(String from, String to, int circleLifeId, int circleLifeDetialId, String content) {
        this.from = from;
        this.to = to;
        this.circleLifeId = circleLifeId;
        this.circleLifeDetialId = circleLifeDetialId;
        this.content = content;
    }

    public EvaEvent(String from, String fromNike, int circleLifeId, int circleLifeDetialId, View view) {
        this.from = from;
        this.fromNike = fromNike;
        this.circleLifeId = circleLifeId;
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

    public int getCircleLifeId() {
        return circleLifeId;
    }

    public void setCircleLifeId(int circleLifeId) {
        this.circleLifeId = circleLifeId;
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
