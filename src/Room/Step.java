package Room;

public class Step{
	public int rowStep;
	public int colStep;
	public int maxStep;

	
	public Step(int rStep, int cStep, int max){
		
		rowStep = rStep;
		colStep = cStep;
		maxStep = max;

	}
	
	public void UpdateValues(int rStep, int cStep, int max){
		
		rowStep = rStep;
		colStep = cStep;
		maxStep = max;

	}
};