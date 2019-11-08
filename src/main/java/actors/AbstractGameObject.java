package actors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({GameObject.class, MovingObject.class})
public abstract class AbstractGameObject {
    @XmlElement
    protected String id;    //global access ID
    @XmlElement
    protected boolean visible;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public boolean isVisible() { return visible; }
    public void setVisibility(boolean state) { this.visible = state; }
}