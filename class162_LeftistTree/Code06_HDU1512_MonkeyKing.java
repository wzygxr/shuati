package class154;

/**
 * HDU 1512 Monkey King - Java实现
 * 
 * 【题目来源】
 * HDU 1512 Monkey King
 * 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1512
 * 
 * 【题目大意】
 * 有n只猴子，每只猴子有一个武力值，开始时每只猴子都是一个独立的群体
 * 每次有两只猴子要打架，它们会从各自群体中找出武力值最大的猴子进行战斗
 * 战斗结束后，两只猴子的武力值各自减半（向下取整），然后两个群体合并
 * 如果两只猴子已经在同一个群体中，则输出-1
 * 
 * 【数据范围】
 * 1 <= n <= 10^5
 * 0 <= 武力值 <= 10^9
 * 
 * 【算法思路】
 * 使用左偏树维护每个群体，支持快速合并和删除最大值操作
 * 结合并查集快速判断两只猴子是否在同一个群体
 * 每次战斗时，从两个群体中删除最大武力值的猴子，武力值减半后重新加入
 * 然后合并两个群体
 * 
 * 【核心操作】
 * 1. 合并操作(merge): O(log n)时间复杂度
 * 2. 删除堆顶(pop): O(log n)时间复杂度
 * 3. 查找操作(find): 近似O(1)时间复杂度（路径压缩优化）
 */

import java.io.*;
import java.util.*;

public class Code06_HDU1512_MonkeyKing {

    /**
     * 最大节点数，根据题目约束设置为100001
     */
    public static int MAXN = 100001;

    /**
     * 节点数量n和操作数量m
     */
    public static int n, m;

    /**
     * power[i] 表示猴子i的武力值
     */
    public static int[] power = new int[MAXN];

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
     * 用于快速找到群体的代表节点
     */
    public static int[] father = new int[MAXN];

    /**
     * 初始化函数，设置每个节点的初始状态
     * 为n个节点初始化左偏树和并查集的数据结构
     * 
     * @timecomplexity O(n) - 遍历每个节点进行初始化
     */
    public static void prepare() {
        // 空节点的距离定义为-1，这是左偏树的基本约定
        // 空节点作为递归终止条件，距离为-1确保计算正确性
        dist[0] = -1;
        
        // 初始化每个节点的状态
        for (int i = 1; i <= n; i++) {
            // 每个节点初始时没有左右子节点，子节点指向空节点(0)
            left[i] = right[i] = 0;
            // 每个节点初始时距离为0（叶子节点到自己的距离为0）
            dist[i] = 0;
            // 每个节点初始时自己是自己的代表节点（并查集）
            // 即每个节点自己构成一个独立的群体
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
        // 这样下次查找时可以直接找到根，大大提高后续查找效率
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
        // 0表示空节点
        if (i == 0 || j == 0) {
            return i + j; // 当一个为空时，返回另一个非空节点
        }
        
        // 维护大根堆性质，确保i是根节点武力值较大的树
        if (power[i] < power[j]) {
            // 交换i和j，确保i始终是根节点武力值更大的树
            int tmp = i;
            i = j;
            j = tmp;
        }
        
        // 递归合并i的右子节点和j
        // 这是左偏树合并的核心策略：总是将另一棵树合并到右子树
        right[i] = merge(right[i], j);
        
        // 维护左偏性质：左子节点的距离不小于右子节点的距离
        // 如果不满足左偏性质，交换左右子节点
        if (dist[left[i]] < dist[right[i]]) {
            // 交换左右子节点以保持左偏性质
            int tmp = left[i];
            left[i] = right[i];
            right[i] = tmp;
        }
        
        // 更新节点i的距离
        // 节点的距离等于右子节点的距离加1
        // 这确保了左偏树的平衡性质
        dist[i] = dist[right[i]] + 1;
        
        // 更新子节点的父节点信息
        // 确保每个子节点的父指针正确指向其父节点
        father[left[i]] = father[right[i]] = i;
        
        return i;
    }

    /**
     * 删除堆顶元素（最大武力值的猴子）
     * 从左偏树中删除最大值节点，并保持左偏树的性质
     * 
     * @param i 堆顶节点编号（即最大武力值的猴子）
     * @return 删除堆顶后新树的根节点编号
     * @timecomplexity O(log n) - 主要开销来自合并左右子树的操作
     * @spacecomplexity O(log n) - 递归调用栈空间，与树高相关
     */
    public static int pop(int i) {
        // 将左右子节点的father设置为自己（解除父子关系）
        // 使左右子树成为独立的子树
        father[left[i]] = left[i];
        father[right[i]] = right[i];
        
        // 并查集有路径压缩，所以i下方的某个节点x，可能有father[x] = i
        // 现在要删掉i了，所以需要将左右子树合并后的新根作为i的代表节点
        // 这样后续通过x找根时仍然能找到正确的根节点
        father[i] = merge(left[i], right[i]);
        
        // 清空节点i的信息，标记为已删除状态
        // 这是为了防止重复访问和错误操作
        left[i] = right[i] = dist[i] = 0;
        
        return father[i];
    }

    /**
     * 模拟一次猴王战斗
     * 
     * @param x 第一只猴子的编号
     * @param y 第二只猴子的编号
     * @return 战斗结果：-1表示在同一群体，否则返回合并后群体的最大战斗力
     * @timecomplexity O(log n) - 主要开销来自合并和删除操作
     */
    public static int fight(int x, int y) {
        // 找到x和y所在的群体代表节点
        int a = find(x);
        int b = find(y);
        
        // 如果在同一个群体，无法战斗
        if (a == b) {
            return -1;
        }
        
        // 从两个群体中取出战斗力最大的猴子
        int l = pop(a);
        int r = pop(b);
        
        // 战斗后武力值减半（向下取整）
        power[a] /= 2;
        power[b] /= 2;
        
        // 重新合并到左偏树中
        father[a] = father[b] = father[l] = father[r] = 
            merge(merge(l, a), merge(r, b));
        
        // 返回合并后群体的最大战斗力
        return power[father[a]];
    }

    /**
     * 主函数，处理输入输出和操作执行
     * 读取输入数据，初始化左偏树，处理每次战斗
     * 
     * @param args 命令行参数
     * @throws IOException 输入输出异常
     * @timecomplexity O(m * log n) - 每次战斗O(log n)
     * @spacecomplexity O(n) - 使用固定大小的数组存储节点信息
     */
    public static void main(String[] args) throws IOException {
        // 使用BufferedReader提高输入效率
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        
        // 处理多组测试数据
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            // 读入n
            n = Integer.parseInt(line);
            
            // 初始化
            prepare();
            
            // 读入每只猴子的武力值
            String[] powerStr = br.readLine().split(" ");
            for (int i = 1; i <= n; i++) {
                power[i] = Integer.parseInt(powerStr[i - 1]);
            }
            
            // 读入m
            m = Integer.parseInt(br.readLine());
            
            // 处理m次战斗
            for (int i = 1; i <= m; i++) {
                // 读取战斗的两只猴子编号
                String[] xy = br.readLine().split(" ");
                int x = Integer.parseInt(xy[0]);
                int y = Integer.parseInt(xy[1]);
                
                // 输出战斗结果
                System.out.println(fight(x, y));
            }
        }
    }
    
    /*
    算法分析：
    
    时间复杂度：
    1. 初始化：O(n)
    2. 合并操作：O(log n)
    3. 删除堆顶：O(log n)
    4. 查找操作：近似O(1)（由于路径压缩）
    5. 总体：O(m * log n)
    
    空间复杂度：O(n)
    
    算法思路：
    1. 使用左偏树维护每个群体，支持快速合并和删除最大值
    2. 使用并查集快速判断两只猴子是否在同一个群体
    3. 每次战斗：
       - 通过并查集判断是否在同一群体
       - 从两个群体中删除最大武力值的猴子
       - 两个猴子武力值减半后重新加入对应群体
       - 合并两个群体
    
    工程化考虑：
    1. 输入输出优化：使用BufferedReader提高读取效率
    2. 异常处理：处理多组测试数据的输入结束条件
    3. 内存管理：合理使用数组，避免动态内存分配
    4. 代码可读性：添加详细注释，清晰的变量命名
    
    与标准库对比：
    1. Java标准库中的PriorityQueue不支持高效合并操作
    2. 左偏树在合并操作上有明显优势
    3. 但在单次操作性能上可能不如优化的二叉堆
    
    调试技巧：
    1. 可以添加打印函数验证左偏树结构
    2. 注意检查并查集的路径压缩是否正确
    3. 特别关注武力值减半后的处理
    
    极端情况：
    1. 所有猴子武力值相同
    2. 只有一只猴子
    3. 所有战斗都在相同群体内（都输出-1）
    
    语言特性差异：
    1. Java中使用BufferedReader提高输入效率
    2. Java中使用System.out.println输出结果
    3. Java中使用/进行整数除法（向下取整）
    4. Java中使用String.split(" ")进行字符串分割
    */
}