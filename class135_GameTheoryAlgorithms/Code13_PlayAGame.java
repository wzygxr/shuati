package class096;

import java.util.Scanner;

// 奇偶性博弈 (Parity Game)
// 一个n*n的棋盘，每一次从角落出发，每次移动到相邻的，而且没有经过的格子上
// 谁不能操作了谁输
// 
// 题目来源：
// 1. HDU 1564 Play a game - http://acm.hdu.edu.cn/showproblem.php?pid=1564
// 2. HDU 1740 A New Stone Game - http://acm.hdu.edu.cn/showproblem.php?pid=1740
// 3. POJ 1704 Georgia and Bob - http://poj.org/problem?id=1704
// 
// 算法核心思想：
// 1. 奇偶性分析：通过分析棋盘的奇偶性判断胜负
// 2. 对称策略：当n为偶数时，后手可以通过对称策略获胜
// 3. 数学规律：n为奇数时先手胜，n为偶数时后手胜
// 
// 时间复杂度分析：
// O(1) - 直接通过数学规律判断
// 
// 空间复杂度分析：
// O(1) - 只需要常数空间
// 
// 工程化考量：
// 1. 异常处理：处理负数输入和边界情况
// 2. 性能优化：直接使用数学规律避免复杂计算
// 3. 可读性：添加详细注释说明算法原理
// 4. 可扩展性：支持不同的棋盘大小
public class Code13_PlayAGame {
    
    // 
    // 算法原理：
    // 1. 棋盘总格子数为n*n
    // 2. 除去起始位置，剩余可走步数为n*n-1
    // 3. 当n*n-1为奇数时，先手胜；为偶数时，后手胜
    // 4. 即当n*n为偶数时，先手胜；为奇数时，后手胜
    // 
    // 证明思路：
    // 1. 当n为偶数时，棋盘可以被完全分割成1*2的多米诺骨牌
    // 2. 后手可以采用对称策略，始终保持优势
    // 3. 当n为奇数时，棋盘有一个中心格子无法被分割
    // 4. 先手可以占据中心位置，破坏对称性获得优势
    // 
    // 数学规律：
    // 当n为奇数时，n*n为奇数，n*n-1为偶数，后手胜
    // 当n为偶数时，n*n为偶数，n*n-1为奇数，先手胜
    public static String solve(int n) {
        // 异常处理：处理非法输入
        if (n <= 0) {
            return "输入非法";
        }
        
        // 特殊情况：n=1时，先手无法移动，后手胜
        if (n == 1) {
            return "2";
        }
        
        // 核心判断逻辑：
        // 当n为偶数时，先手胜；当n为奇数时，后手胜
        return (n & 1) == 0 ? "1" : "2";
    }
    
    // 测试函数
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // 读取输入
        while (scanner.hasNextInt()) {
            int n = scanner.nextInt();
            
            // 终止条件
            if (n == 0) {
                break;
            }
            
            // 计算结果并输出
            System.out.println(solve(n));
        }
        
        scanner.close();
    }
}