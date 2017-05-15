package Room;

import AccountList.Account;
import Server.TCPServerHandler;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

public class DefaultRoom  implements Room{
	public ChannelGroup channelGroup ;
	public int[][] matrix ;
	public int winCondition = 5;
	public int maxCol = 15;
	public int maxRow = 15;
	public DefaultRoom(ChannelGroup channelGroup, int[][] matrix) {
		this.channelGroup = channelGroup;
		this.matrix = matrix;
	}
	@Override
	public boolean isFull() {
		if(channelGroup.size() == 2 ) return true;
		else return false;
	}

	@Override
	public void addPlayer(Account account) {
		this.channelGroup.add(account.getChannel());
		
	}

	@Override
	public void removePlayer(Account account) {
		this.channelGroup.remove(account.getChannel());
		account.setRoom(0);
	}

	@Override
	public int getSize() {
		return this.channelGroup.size();
	}

	@Override
	public void setSize(int size) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Account getPlayer(int id) {
		return TCPServerHandler.All_ACCOUNT.getAcc(id);
	}
	
	@Override
	public ChannelGroup getChannelGroup() {
		return this.channelGroup;
	}
	@Override
	public void setFlag(boolean isRoot,int row, int col) {
		if(isRoot)
			matrix[row][col] = 1;
		else 
			matrix[row][col] =	2;
		
	}
	@Override
	public boolean checkWin(boolean isRoot,int row, int col) {
	
		int checkValue =0;
		if(isRoot) checkValue =1 ;
		else checkValue =2;
		int rowValue = CheckRow(checkValue, row, col);
		int colValue = CheckColumn(checkValue, row, col);
		
		int leftDiagonalValue = CheckLeftDiagonal(checkValue, row, col);
		int rightDiagonalValue = CheckRightDiagonal(checkValue, row, col);
		if(rowValue >=winCondition || colValue >=winCondition
				||leftDiagonalValue >= winCondition || rightDiagonalValue >= winCondition)
			return true;
		return false;
	}
	
	int CheckRow(int checkValue, int row, int col)
    {

        int count = 1;
        int stepTry = winCondition - 1;
        int limit = maxCol - 1;

        //determine number of square that can be checked
        int numberOfStep = CalculatePossibeSteps(col, stepTry, limit);
        //check square on the right
        Step step = new Step(0, 1, numberOfStep);
        count += NumberOfMatch(row, col, step, checkValue);

        stepTry = -(winCondition - 1);
        limit = 0;
        //determine number of square that can be checked
        numberOfStep = CalculatePossibeSteps(col, stepTry, limit);
        //check square on the left
        step.UpdateValues(0, -1, numberOfStep);

        count += NumberOfMatch(row, col, step, checkValue);
        return count;


    }

    int CheckColumn(int checkValue, int row, int col)
    {

        int count = 1;
        int stepTry = winCondition - 1;
        int limit = maxRow - 1;

        //determine number of square that can be checked
        int numberOfStep = CalculatePossibeSteps(row, stepTry, limit);
        //check square on below
        Step step = new Step(1, 0, numberOfStep);

        count += NumberOfMatch(row, col, step, checkValue);

        stepTry = -(winCondition - 1);
        limit = 0;
        //determine number of square that can be checked
        numberOfStep = CalculatePossibeSteps(row, stepTry, limit);

        //check square on the left
        step.UpdateValues(-1, 0, numberOfStep);

        count += NumberOfMatch(row, col, step, checkValue);
        return count;


    }

    int CheckLeftDiagonal(int checkValue, int row, int col)
    {

        int count = 1;
        int stepTry = winCondition - 1;
        int limit = maxRow - 1;

        //determine number of square that can be checked
        int numberOfStep = CalculatePossibeSteps(row, stepTry, limit);

        limit = maxCol - 1;
        int tmp = CalculatePossibeSteps(col, stepTry, limit);
        numberOfStep = numberOfStep < tmp ? numberOfStep : tmp;

        //check square on down right
        Step step = new Step(1, 1, numberOfStep);

        count += NumberOfMatch(row, col, step, checkValue);

        stepTry = -(winCondition - 1);
        limit = 0;
        //determine number of square that can be checked
        numberOfStep = CalculatePossibeSteps(row, stepTry, limit);

        limit = 0;
        tmp = CalculatePossibeSteps(col, stepTry, limit);
        numberOfStep = numberOfStep < tmp ? numberOfStep : tmp;

        //check square on the up right
        step.UpdateValues(-1, -1, numberOfStep);

        count += NumberOfMatch(row, col, step, checkValue);
        return count;
    }

    int CheckRightDiagonal(int checkValue, int row, int col)
    {

        int count = 1;
        int stepTry = -(winCondition - 1);
        int limit = 0;

        //determine number of square that can be checked
        int numberOfStep = CalculatePossibeSteps(row, stepTry, limit);

        stepTry = winCondition - 1;
        limit = maxCol - 1;
        int tmp = CalculatePossibeSteps(col, stepTry, limit);
        numberOfStep = numberOfStep < tmp ? numberOfStep : tmp;

        //check square on up right
        Step step = new Step(-1, 1, numberOfStep);

        count += NumberOfMatch(row, col, step, checkValue);

        stepTry = winCondition - 1;
        limit = maxRow - 1;
        //determine number of square that can be checked
        numberOfStep = CalculatePossibeSteps(row, stepTry, limit);

        stepTry = -(winCondition - 1);
        limit = 0;
        tmp = CalculatePossibeSteps(col, stepTry, limit);
        numberOfStep = numberOfStep < tmp ? numberOfStep : tmp;

        //check square on the down left
        step.UpdateValues(1, -1, numberOfStep);

        count += NumberOfMatch(row, col, step, checkValue);
        return count;
    }

    int NumberOfMatch(int row, int col, Step step, int checkValue)
    {

        int rStep = step.rowStep;
        int cStep = step.colStep;
        int maxStep = step.maxStep;

        int count = 0;

        for (int i = 1; i <= maxStep; ++i)
        {

            int value = matrix[row + i * rStep][col + i * cStep];

            if (value != checkValue) return count;

            ++count;
        }

        return count;
    }
    int CalculatePossibeSteps(int initial, int steps, int limit)
    {

        int result = Math.abs(steps);
        // upper limit
        if (steps > 0)
        {

            if (initial + steps > limit)
            {
                result = limit - initial;
            }
        }
        else
        {
            if (initial + steps < limit)
            {
                result = initial - limit;
            }
        }

        return result;
    }

    int CheckRange(int value, int min, int max)
    {

        if (value > max)
        {
            value = max;
        }
        else if (value < min)
        {
            value = min;
        }

        return value;
    }
	@Override
	public void resetMatrix() {
		System.out.println("matrix leng" + matrix.length);
		for (int i = 0; i < matrix.length; i++) {
				for(int j =0;j<15;j++)
				{
					matrix[i][j] = 0;
				}
		}
		
	}
	

	



}
