package design_patterns._16_mediator;

public abstract class UIComponent {
    protected UIMediator mediator;

    public UIComponent(UIMediator mediator) {
        this.mediator = mediator;
    }

    public void notifyMediator() {
        mediator.componentChanged(this);
    }
}