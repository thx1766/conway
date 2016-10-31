import java.lang.Math;

public class conway {
	public static void main(String args[]){
		System.out.println("Nat's implementation of Conway's Game of Life");


		//these values are picked to optimize for CMD max window size
		//int hsize = 70;
		//int vsize = 50;
		
		int hsize = 60, vsize = 40;//eclipse console maximized
		
		//eclipse vertical screen
		//int hsize = 50, vsize = 50;
		
		int[][] board = new int[vsize][hsize];

		board = random_populate(board, hsize, vsize);		
		//board = glider_init(board, hsize, vsize);
		
		display(board, hsize, vsize);
		int runs = 0, maxruns = 3;
		//while(runs<maxruns){
		while(true){
			for(int x = 0; x<100; x++) { System.out.print("-"); } System.out.println(runs);
			board = update(board, hsize, vsize);
			display(board, hsize, vsize);
			try{Thread.sleep(300);}catch(Exception e){}
			runs++;
		}

		
	}

	public static int[][] random_populate(int board[][], int hsize, int vsize){
		for(int i = 0; i<vsize; i++){
			for(int j = 0; j<hsize; j++){
				//this value must be adjusted for optimal
				if(Math.random() > .9)
					board[i][j] = 1;
				else
					board[i][j] = 0;
			}
		}
		return board;
	}
	
	public static int[][] glider_init(int board[][], int hsize, int vsize){
		//seems to be broken. Need to more carefully study the starting state
		for(int i = 0; i<hsize; i++)
			for(int j = 0; j<vsize; j++)
				board[j][i]=0;
		
		board[1][25]=1;
		board[2][23]=1;	board[2][25]=1;
		board[3][13]=1;	board[3][14]=1;	board[3][21]=1;	board[3][22]=1;	board[3][35]=1;	board[3][36]=1;
		board[4][12]=1;	board[4][16]=1;	board[4][21]=1;	board[4][22]=1;	board[4][35]=1;	board[4][36]=1;
		board[5][1]=1;	board[5][2]=1;	board[5][11]=1;	board[5][17]=1; board[5][21]=1; board[5][22]=1;
		board[6][1]=1;	board[6][2]=1;	board[6][11]=1;	board[6][15]=1;	board[6][17]=1;	board[6][18]=1; board[6][23]=1; board[6][25]=1;
		board[7][11]=1;	board[7][17]=1; board[7][25]=1;
		board[8][12]=1;	board[8][16]=1;
		board[9][13]=1; board[9][14]=1;
		
		return board;
	}
	
	public static void display(int board[][], int hsize, int vsize){
		for(int i=0; i<vsize; i++){
			for(int j=0; j<hsize; j++){
				if(board[i][j]==1)
					System.out.print("[+]");
				else
					System.out.print("[ ]");
			}
			System.out.println();
		}

	}

	public static int[][] update(int board[][], int hsize, int vsize){
	//takes board and updates one tick
	//rules:
	//	1) a live cell with fewer than two live neighbors dies (under population)
	//	2) a live cell with two or three cells survives
	//	3) a live cell with more than three live neighbors dies (over population)
	//	4) a dead cell with exactly three live neighbors becomes alive (reproduction)
		
		for(int i = 0; i<vsize; i++){
			for(int j=0; j<hsize; j++){
				int n = 0;
				n = neighbors(board, vsize, hsize, i, j);
				if(board[i][j] == 1){//cell starts alive, can die or stay alive
					if(n<2)//underpopulation
						board[i][j]=0;//death
					else if(n == 2 || n == 3)//survival
						board[i][j]=1;//maybe not needed, inefficient reassign of 1 to value already holding 1
					else if(n>3)//overpopulation
						board[i][j]=0;//death
				}else{//cell starts dead, can come alive or stay dead
					if(n==3)
						board[i][j]=1;
				}
			}
		}
		return board;
	}

	public static int neighbors(int board[][], int vsize, int hsize, int yloc, int xloc){
	//determines the number of living neighbors each alive or dead cell has
	//very inefficient to calculate data for each cell in the board independent
		int neighbors = 0;
	//typical case (non-edge): calculate neighbors based on:
	//
	//		1	2	3
	//		4	X	5
	//		6	7	8
	//
	//	1)	left top		yloc+1	xloc-1
	//	2)	center top		yloc+1	xloc
	//	3)	right top		yloc+1	xloc+1
	//	4)	center left		yloc	xloc-1
	//	5)	center right	yloc	xloc+1
	//	6)	left bottom		yloc-1	xloc-1
	//	7)	center bottom	yloc-1	xloc
	//	8)	right bottom	yloc-1	xloc+1
	//
	//special cases: edges
		
	//IDEA: rather than building code for every edge location, just catch indexoutofbounds exceptions
	//will result in messy overuse of try/catch, but it will get it working
		
		//try: test if specified neighbor is alive or dead
		//catch: IndexOutOfBoundsException: edge detected, treat as dead cell and don't increment neighbor count
		int edgecount = 0;
		/*1*/try{ if(board[yloc+1][xloc-1] == 1) neighbors++; } catch(IndexOutOfBoundsException e){/*System.out.print("1");*/ edgecount++;}
		
		/*2*/try{ if(board[yloc+1][xloc] == 1) neighbors++; } catch(IndexOutOfBoundsException e){/*System.out.print("2");*/ edgecount++;}
		
		/*3*/try{ if(board[yloc+1][xloc+1] == 1) neighbors++; } catch(IndexOutOfBoundsException e){/*System.out.print("3");*/ edgecount++;}
		
		/*4*/try{ if(board[yloc][xloc-1] == 1) neighbors++;} catch(IndexOutOfBoundsException e){/*System.out.print("4");*/edgecount++;}
		
		/*5*/try{ if(board[yloc][xloc+1] == 1) neighbors++;} catch(IndexOutOfBoundsException e){/*System.out.print("5");*/edgecount++;}
		
		/*6*/try{ if(board[yloc-1][xloc-1] == 1) neighbors++;} catch(IndexOutOfBoundsException e){/*System.out.print("6");*/edgecount++;}
		
		/*7*/try{ if(board[yloc-1][xloc] == 1) neighbors++;} catch(IndexOutOfBoundsException e){/*System.out.print("7");*/edgecount++;}
		
		/*8*/try{ if(board[yloc-1][xloc+1] == 1) neighbors++;} catch(IndexOutOfBoundsException e){/*System.out.print("8");*/edgecount++;}
		boolean debug = false;
		if(debug) System.out.println("edgecount: "+edgecount);
		return neighbors;
	}
}
