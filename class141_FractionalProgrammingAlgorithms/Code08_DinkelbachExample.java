package class138;

/**
 * Dinkelbach算法示例 - 01分数规划的高效迭代解法
 * 
 * <h3>题目信息</h3>
 * <ul>
 *   <li><strong>题目描述</strong>：给定n个物品，每个物品有两个属性a[i]和b[i]。
 *   选择一些物品使得选中物品的a值和与b值和的比值最大。</li>
 *   <li><strong>数据范围</strong>：
 *     <ul>
 *       <li>1 <= n <= 100000（物品数量）</li>
 *       <li>1 <= a[i], b[i] <= 100</li>
 *     </ul>
 *   </li>
 * </ul>
 * 
 * <h3>算法思路</h3>
 * <p>使用Dinkelbach算法求解01分数规划问题，相比二分法具有更快的收敛速度：</p>
 * <ol>
 *   <li><strong>迭代逼近</strong>：通过迭代方式不断逼近最优解</li>
 *   <li><strong>贪心选择</strong>：每次迭代选择d[i] > 0的物品</li>
 *   <li><strong>收敛判断</strong>：根据精度要求判断是否收敛</li>
 * </ol>
 * 
 * <h3>数学原理</h3>
 * <p>Dinkelbach算法的核心思想：</p>
 * <p>设当前比率为L，计算d[i] = a[i] - L * b[i]，选择所有d[i] > 0的物品，
 * 得到新的比率L' = (Σa[i]) / (Σb[i])，重复此过程直到收敛。</p>
 * 
 * <h3>复杂度分析</h3>
 * <ul>
 *   <li><strong>时间复杂度</strong>：O(k * n)，其中k是迭代次数，通常为O(log(1/ε))</li>
 *   <li><strong>空间复杂度</strong>：O(n)</li>
 * </ul>
 * 
 * <h3>与二分法的对比</h3>
 * <ul>
 *   <li><strong>优点</strong>：收敛速度更快，通常只需要几次迭代</li>
 *   <li><strong>缺点</strong>：实现相对复杂，需要处理迭代收敛</li>
 *   <li><strong>适用场景</strong>：大规模数据，对性能要求高的场景</li>
 * </ul>
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code08_DinkelbachExample {

    // 常量定义
    public static final int MAXN = 100001;  // 最大物品数量
    public static final double EPSILON = 1e-9; // 精度要求
    
    // 物品属性数组
    public static int[] a = new int[MAXN];  // 收益值数组
    public static int[] b = new int[MAXN];  // 代价值数组
    
    // 结余值数组：d[i] = a[i] - L * b[i]
    public static double[] d = new double[MAXN];
    
    // 全局变量，存储物品数量
    public static int n;
    
    /**
     * Dinkelbach算法求解01分数规划
     * 
     * <p>算法流程：</p>
     * <ol>
     *   <li>初始化比率值L = 0</li>
     *   <li>循环迭代直到收敛：
     *     <ul>
     *       <li>计算每个物品的结余值d[i] = a[i] - L * b[i]</li>
     *       <li>贪心选择所有d[i] > 0的物品</li>
     *       <li>计算新的比率值L' = (Σa[i]) / (Σb[i])</li>
     *       <li>如果|L' - L| < ε 或 sumD <= 0，则停止迭代</li>
     *     </ul>
     *   </li>
     * </ol>
     * 
     * @return 最优比率值
     */
    public static double dinkelbach() {
        double L = 0.0; // 初始比率值
        
        while (true) {
            // 根据当前比率值L计算每个物品的结余值
            for (int i = 1; i <= n; i++) {
                d[i] = a[i] - L * b[i];
            }
            
            // 贪心选择：选择所有结余值为正的物品
            double sumD = 0.0;  // 结余值总和
            double sumA = 0.0;  // 选中物品的a值和
            double sumB = 0.0;  // 选中物品的b值和
            
            for (int i = 1; i <= n; i++) {
                if (d[i] > 0) {
                    sumD += d[i];
                    sumA += a[i];
                    sumB += b[i];
                }
            }
            
            // 收敛判断：如果结余值总和<=0，说明已经找到最优解
            if (sumD <= 0) {
                return L;
            }
            
            // 计算新的比率值
            double newL = sumA / sumB;
            
            // 收敛判断：如果新旧比率值差小于精度要求，则停止迭代
            if (Math.abs(newL - L) < EPSILON) {
                return newL;
            }
            
            // 更新比率值，继续迭代
            L = newL;
        }
    }
    
    /**
     * 主函数：处理输入输出，执行Dinkelbach算法
     * 
     * <p>算法流程：</p>
     * <ol>
     *   <li>读取输入数据（物品数量、每个物品的a值和b值）</li>
     *   <li>调用Dinkelbach算法求解最优比率</li>
     *   <li>输出结果，保留6位小数</li>
     * </ol>
     * 
     * @param args 命令行参数
     * @throws IOException 输入输出异常
     */
    public static void main(String[] args) throws IOException {
        // 初始化输入输出流
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        try {
            // 读取物品数量
            in.nextToken();
            n = (int) in.nval;
            
            // 读取每个物品的a值（收益值）
            for (int i = 1; i <= n; i++) {
                in.nextToken();
                a[i] = (int) in.nval;
            }
            
            // 读取每个物品的b值（代价值）
            for (int i = 1; i <= n; i++) {
                in.nextToken();
                b[i] = (int) in.nval;
            }
            
            // 调用Dinkelbach算法求解最优比率
            double result = dinkelbach();
            
            // 输出结果，保留6位小数
            out.printf("%.6f\n", result);
            out.flush();
            
        } finally {
            // 确保资源正确关闭
            try {
                out.close();
                br.close();
            } catch (Exception e) {
                // 忽略关闭时的异常
            }
        }
    }
}