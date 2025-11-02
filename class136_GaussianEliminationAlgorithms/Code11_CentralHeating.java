package class133;

// POJ 2345 Central heating
// 题目链接：http://poj.org/problem?id=2345
// 题目大意：有n个阀门控制中央供暖系统，每个阀门可以是开(1)或关(0)状态
// 有m个技术人员，每个技术人员负责一些阀门，当技术人员工作时，他们会切换他们负责的阀门状态
// 给出每个技术人员工作时负责的阀门和最终所有阀门都应该是开的状态
// 求最少需要哪些技术人员工作才能达到目标状态
//
// 解题思路：
// 1. 这是一个异或方程组问题，可以使用高斯消元法求解
// 2. 对于每个阀门，我们可以建立一个方程表示其最终状态
// 3. 对于第i个阀门，其最终状态 = 初始状态 XOR 所有影响它的技术人员工作情况
// 4. 通过高斯消元法求解这个异或方程组，得到最少需要哪些技术人员工作

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Code11_CentralHeating {

    public static int MAXN = 305;
    
    // 增广矩阵，用于高斯消元求解异或方程组
    // mat[i][j] 表示第j个技术人员是否负责第i个阀门（1表示负责，0表示不负责）
    // mat[i][m+1] 表示第i个阀门的目标状态与初始状态的异或值
    public static int[][] mat = new int[MAXN][MAXN];
    
    // 结果数组，result[i] 表示第i个技术人员是否工作（1表示工作，0表示不工作）
    public static int[] result = new int[MAXN];
    
    // 阀门数量和技术人员数量
    public static int n, m;
    
    /**
     * 高斯消元法求解异或方程组
     * 时间复杂度: O(n^3)
     * 空间复杂度: O(n^2)
     * 
     * 数学原理：
     * 异或方程组形式：
     * a11*x1 XOR a12*x2 XOR ... XOR a1m*xm = b1
     * a21*x1 XOR a22*x2 XOR ... XOR a2m*xm = b2
     * ...
     * an1*x1 XOR an2*x2 XOR ... XOR anm*xm = bn
     * 
     * 其中：
     * - xi 表示第i个技术人员是否工作（1表示工作，0表示不工作）
     * - aij 表示第j个技术人员是否负责第i个阀门（1表示负责，0表示不负责）
     * - bi 表示第i个阀门的目标状态与初始状态的异或值（题目中初始状态都是关，目标状态都是开）
     * 
     * 算法步骤：
     * 1. 对于每一列col，找到一个行pivotRow使得mat[pivotRow][col] = 1
     * 2. 将该行与第row行交换
     * 3. 用第row行消除其他所有行的第col列系数
     * 4. 检查是否有解
     * 5. 回代求解
     * 
     * @return 是否有解
     */
    public static boolean gauss() {
        int row = 1;
        
        // 对每一列进行处理
        for (int col = 1; col <= m && row <= n; col++) {
            // 寻找第col列中系数为1的行，将其交换到第row行
            // 这一步是为了确保主元不为0，便于后续的消元操作
            int pivotRow = row;
            for (int i = row; i <= n; i++) {
                if (mat[i][col] == 1) {
                    pivotRow = i;
                    break;
                }
            }
            
            // 如果找不到系数为1的行，说明该列全为0，继续处理下一列
            if (mat[pivotRow][col] == 0) {
                continue;
            }
            
            // 将找到的行与第row行交换，确保主元在对角线上
            if (pivotRow != row) {
                for (int j = 1; j <= m + 1; j++) {
                    int tmp = mat[row][j];
                    mat[row][j] = mat[pivotRow][j];
                    mat[pivotRow][j] = tmp;
                }
            }
            
            // 用第row行消除其他行的第col列系数
            // 对于每一行i，如果i != row且mat[i][col] == 1，则用第row行消除第i行的第col列系数
            for (int i = 1; i <= n; i++) {
                if (i != row && mat[i][col] == 1) {
                    // 对于第i行，将其与第row行进行异或操作，消除第col列的系数
                    for (int j = 1; j <= m + 1; j++) {
                        mat[i][j] ^= mat[row][j]; // 异或操作
                    }
                }
            }
            
            row++;
        }
        
        // 检查是否有解
        // 从第row行开始检查，如果某一行的系数全为0但常数项不为0，则无解
        for (int i = row; i <= n; i++) {
            if (mat[i][m + 1] != 0) {
                return false; // 无解
            }
        }
        
        // 回代求解
        // 从第min(row-1, m)行开始，逐行求解变量的值
        for (int i = Math.min(row - 1, m); i >= 1; i--) {
            result[i] = mat[i][m + 1]; // 初始化为常数项
            // 对于第i个变量，需要考虑其他变量对其的影响
            for (int j = i + 1; j <= m; j++) {
                result[i] ^= (mat[i][j] & result[j]); // 异或操作
            }
        }
        
        return true; // 有解
    }
    
    /**
     * 主函数
     * 读取输入数据，构建系数矩阵，调用高斯消元法求解，输出结果
     * 
     * 算法流程：
     * 1. 读取阀门数量n和技术人员数量m
     * 2. 初始化增广矩阵
     * 3. 读取每个技术人员负责的阀门
     * 4. 设置常数项（所有阀门都需要变为开状态，初始状态都是关）
     * 5. 使用高斯消元法求解
     * 6. 输出结果
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        String line = br.readLine().trim();
        if (line == null || line.isEmpty()) {
            out.flush();
            out.close();
            br.close();
            return;
        }
        
        n = Integer.parseInt(line);
        m = n; // 题目保证n个方程，n个未知数
        
        // 初始化矩阵，将所有元素置为0
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m + 1; j++) {
                mat[i][j] = 0;
            }
        }
        
        // 读取每个技术人员负责的阀门
        List<List<Integer>> technicians = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            technicians.add(new ArrayList<>());
        }
        
        for (int i = 1; i <= n; i++) {
            line = br.readLine().trim();
            String[] parts = line.split(" ");
            for (String part : parts) {
                if (!part.isEmpty()) {
                    int valve = Integer.parseInt(part);
                    if (valve > 0) {
                        technicians.get(i - 1).add(valve);
                        mat[valve][i] = 1; // 第i个技术人员负责第valve个阀门
                    }
                }
            }
        }
        
        // 设置常数项（所有阀门都需要变为开状态，初始状态都是关）
        // 题目中初始状态都是关(0)，目标状态都是开(1)，所以异或值为1
        for (int i = 1; i <= n; i++) {
            mat[i][m + 1] = 1; // 目标状态都是开
        }
        
        // 使用高斯消元法求解异或方程组
        if (gauss()) {
            // 输出结果（需要工作的技术人员编号）
            boolean first = true;
            for (int i = 1; i <= m; i++) {
                if (result[i] == 1) {
                    if (!first) {
                        out.print(" ");
                    }
                    out.print(i);
                    first = false;
                }
            }
            out.println();
        } else {
            out.println("No solution");
        }
        
        out.flush();
        out.close();
        br.close();
    }
}