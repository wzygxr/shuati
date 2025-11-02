package class154;

/**
 * 派遣问题 - Java实现
 * 
 * 【题目来源】
 * 洛谷 P1552 [APIO2012] 派遣
 * 题目链接: https://www.luogu.com.cn/problem/P1552
 * 
 * 【题目大意】
 * 一共有n个忍者，每个忍者有上级编号、工资、能力，三个属性
 * 输入保证，任何忍者的上级编号 < 这名忍者的编号，1号忍者是整棵忍者树的头
 * 你一共有m的预算，可以在忍者树上随意选一棵子树，然后在这棵子树上挑选忍者
 * 你选择某棵子树之后，不一定要选子树头的忍者，只要不超过m的预算，可以随意选择子树上的忍者
 * 最终收益 = 雇佣人数 * 子树头忍者的能力，返回能取得的最大收益是多少
 * 
 * 【数据范围】
 * 1 <= n <= 10^5
 * 1 <= m <= 10^9
 * 1 <= 每个忍者工资 <= m
 * 1 <= 每个忍者领导力 <= 10^9
 * 
 * 【算法思路】
 * 使用左偏树维护每个子树中的忍者，支持快速删除最大工资的忍者
 * 结合树形DP，从下往上处理每个子树
 * 对于每个子树，维护一个大根堆，存储该子树中的忍者
 * 当子树总工资超过预算时，不断删除工资最高的忍者
 * 计算以当前节点为领导时的最大收益
 * 
 * 【核心操作】
 * 1. 合并操作(merge): O(log n)时间复杂度
 * 2. 删除堆顶(pop): O(log n)时间复杂度
 * 3. 查找操作(find): 近似O(1)时间复杂度（路径压缩优化）
 * 
 * 【提交说明】
 * 提交时请把类名改成"Main"，可以通过所有测试用例
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code04_Dispatch1 {

    /**
     * 最大节点数，根据题目约束设置为100001
     */
    public static int MAXN = 100001;

    /**
     * 节点数量n和预算m
     */
    public static int n, m;

    /**
     * leader[i] 表示忍者i的上级编号
     */
    public static int[] leader = new int[MAXN];

    /**
     * cost[i] 表示忍者i的工资
     */
    public static long[] cost = new long[MAXN];

    /**
     * ability[i] 表示忍者i的能力（领导力）
     */
    public static long[] ability = new long[MAXN];

    /**
     * left[i] 表示节点i的左子节点
     */
    public static int[] left = new int[MAXN];

    /**
     * right[i] 表示节点i的右子节点
     */
    public static int[] right = new int[MAXN];

    /**
     * dist[i] 表示节点i的距离（到最近外节点的边数）
     * 距离定义：节点到其子树中最近的外节点（左子树或右子树为空的节点）的边数
     */
    public static int[] dist = new int[MAXN];

    /**
     * father[i] 表示节点i在并查集中的父节点
     * 用于快速找到堆的根节点
     */
    public static int[] father = new int[MAXN];

    /**
     * size[i] 表示以节点i为根的堆的大小（忍者数量）
     */
    public static int[] size = new int[MAXN];

    /**
     * sum[i] 表示以节点i为根的堆的费用和（工资总和）
     */
    public static long[] sum = new long[MAXN];

    /**
     * 初始化函数，设置每个节点的初始状态
     * 为n个节点初始化左偏树和相关数据结构
     * 
     * @timecomplexity O(n) - 遍历每个节点进行初始化
     */
    public static void prepare() {
        // 空节点的距离定义为-1，这是左偏树的基本约定
        dist[0] = -1;
        
        // 初始化每个节点的状态
        for (int i = 1; i <= n; i++) {
            // 每个节点初始时没有左右子节点
            left[i] = right[i] = 0;
            // 每个节点初始时距离为0
            dist[i] = 0;
            // 每个节点初始时堆大小为1（只有自己）
            size[i] = 1;
            // 每个节点初始时费用和为自己的工资
            sum[i] = cost[i];
            // 每个节点初始时自己是自己的代表节点
            father[i] = i;
        }
    }

    /**
     * 并查集查找函数，带路径压缩优化
     * 查找节点i所在集合的代表元素（根节点）
     * 
     * @param i 要查找的节点编号
     * @return 节点i所在集合的代表元素
     * @timecomplexity O(α(n)) - 近似常数时间，α是阿克曼函数的反函数
     * @spacecomplexity O(α(n)) - 递归调用栈空间
     */
    public static int find(int i) {
        // 路径压缩优化：递归查找过程中将路径上的所有节点直接连到根节点
        return father[i] = (father[i] == i) ? i : find(father[i]);
    }

    /**
     * 合并两棵左偏树，维护大根堆性质
     * 左偏树合并是其核心操作，通过递归方式将两棵左偏树合并为一棵
     * 
     * @param i 第一棵左偏树的根节点编号
     * @param j 第二棵左偏树的根节点编号
     * @return 合并后新树的根节点编号
     * @timecomplexity O(log n) - 合并操作的时间复杂度与树高相关，由于左偏性质，树高不超过O(log n)
     * @spacecomplexity O(log n) - 递归调用栈空间，与树高相关
     */
    public static int merge(int i, int j) {
        // 递归终止条件：如果其中一个节点为空，返回另一个节点
        if (i == 0 || j == 0) {
            return i + j;
        }
        
        // 维护大根堆性质，确保i是根节点工资较大的树
        if (cost[i] < cost[j]) {
            // 交换i和j，确保i始终是根节点工资更大的树
            int tmp = i;
            i = j;
            j = tmp;
        }
        
        // 递归合并i的右子节点和j
        right[i] = merge(right[i], j);
        
        // 维护左偏性质：左子节点的距离不小于右子节点的距离
        if (dist[left[i]] < dist[right[i]]) {
            // 交换左右子节点以保持左偏性质
            int tmp = left[i];
            left[i] = right[i];
            right[i] = tmp;
        }
        
        // 更新节点i的距离
        dist[i] = dist[right[i]] + 1;
        
        // 更新子节点的父节点信息
        father[left[i]] = father[right[i]] = i;
        
        return i;
    }

    /**
     * 删除堆顶元素（最大工资的忍者）
     * 从左偏树中删除最大值节点，并保持左偏树的性质
     * 
     * @param i 堆顶节点编号
     * @return 删除堆顶后新树的根节点编号
     * @timecomplexity O(log n) - 主要开销来自合并左右子树的操作
     * @spacecomplexity O(log n) - 递归调用栈空间，与树高相关
     */
    public static int pop(int i) {
        // 将左右子节点的father设置为自己（解除父子关系）
        father[left[i]] = left[i];
        father[right[i]] = right[i];
        
        // 合并左右子树，作为新的根
        father[i] = merge(left[i], right[i]);
        
        // 清空节点i的信息
        left[i] = right[i] = dist[i] = 0;
        
        return father[i];
    }

    /**
     * 计算最大收益
     * 使用树形DP结合左偏树优化
     * 
     * @return 最大收益
     * @timecomplexity O(n * log n) - 每个节点可能需要多次删除操作
     * @spacecomplexity O(n) - 使用固定大小的数组存储节点信息
     */
    public static long compute() {
        // 最大收益
        long ans = 0;
        
        // 从下往上处理每个节点
        for (int i = n; i >= 1; i--) {
            // 找到当前节点所在堆的根节点
            int h = find(i);
            // 获取堆的大小和费用和
            int hsize = size[h];
            long hsum = sum[h];
            
            // 如果费用和超过预算，不断删除工资最高的忍者
            while (hsum > m) {
                // 删除堆顶元素
                pop(h);
                // 更新堆大小和费用和
                hsize--;
                hsum -= cost[h];
                // 重新找到根节点
                h = find(i);
            }
            
            // 更新最大收益：雇佣人数 * 子树头忍者的能力
            ans = Math.max(ans, (long) hsize * ability[i]);
            
            // 如果不是根节点，需要与父节点合并
            if (i > 1) {
                // 找到父节点所在堆的根节点
                int p = find(leader[i]);
                // 获取父节点堆的大小和费用和
                int psize = size[p];
                long psum = sum[p];
                
                // 合并当前堆和父节点堆
                father[p] = father[h] = merge(p, h);
                
                // 更新合并后堆的大小和费用和
                size[father[p]] = psize + hsize;
                sum[father[p]] = psum + hsum;
            }
        }
        
        return ans;
    }

    /**
     * 主函数，处理输入输出和操作执行
     * 读取输入数据，初始化数据结构，计算最大收益
     * 
     * @param args 命令行参数
     * @throws IOException 输入输出异常
     * @timecomplexity O(n * log n) - 主要开销来自compute函数
     * @spacecomplexity O(n) - 使用固定大小的数组存储节点信息
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读入n和m
        in.nextToken();
        n = (int) in.nval;
        in.nextToken();
        m = (int) in.nval;
        
        // 读入每个忍者的信息
        for (int i = 1; i <= n; i++) {
            in.nextToken();
            leader[i] = (int) in.nval;  // 上级编号
            in.nextToken();
            cost[i] = (int) in.nval;    // 工资
            in.nextToken();
            ability[i] = (int) in.nval; // 能力
        }
        
        // 初始化
        prepare();
        
        // 计算并输出最大收益
        out.println(compute());
        
        out.flush();
        out.close();
        br.close();
    }
    
    /*
    算法分析：
    
    时间复杂度：
    1. 初始化：O(n)
    2. 合并操作：O(log n)
    3. 删除堆顶：O(log n)
    4. 查找操作：近似O(1)（由于路径压缩）
    5. 总体：O(n * log n)
    
    空间复杂度：O(n)
    
    算法思路：
    1. 使用左偏树维护每个子树中的忍者，支持快速删除最大工资的忍者
    2. 结合树形DP，从下往上处理每个子树
    3. 对于每个子树，维护一个大根堆，存储该子树中的忍者
    4. 当子树总工资超过预算时，不断删除工资最高的忍者
    5. 计算以当前节点为领导时的最大收益
    
    工程化考虑：
    1. 输入输出优化：使用StreamTokenizer和PrintWriter提高效率
    2. 内存管理：使用静态数组避免动态内存分配
    3. 数据类型：使用long类型避免整数溢出
    4. 代码可读性：添加详细注释，清晰的变量命名
    
    与标准库对比：
    1. Java标准库中的PriorityQueue不支持高效合并操作
    2. 左偏树在合并操作上有明显优势
    3. 但在单次操作性能上可能不如优化的二叉堆
    
    调试技巧：
    1. 可以添加打印函数验证左偏树结构
    2. 注意检查并查集的路径压缩是否正确
    3. 特别关注预算超支时的删除操作
    
    极端情况：
    1. 所有忍者工资相同
    2. 预算非常大，可以雇佣所有忍者
    3. 预算非常小，只能雇佣一个忍者
    */
}
