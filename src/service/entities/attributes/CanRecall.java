package src.service.entities.attributes;

public interface CanRecall {
	public Position getRecallPosition();

	public void setRecallPosition(Position position);

	public void recallEntity();
}
