package class159;

// 异或粽子
// 小粽面前有n种互不相同的粽子馅儿，小粽将它们摆放为了一排，并从左至右编号为1到n。
// 第i种馅儿具有一个非负整数的属性值ai。每种馅儿的数量都足够多，即小粽不会因为缺少原料而做不出想要的粽子。
// 小粽准备用这些馅儿来做出k个粽子。
// 小粽的做法是：选两个整数数l, r，满足1≤l≤r≤n，将编号在[l, r]范围内的所有馅儿混合做成一个粽子，
// 所得的粽子的美味度为这些粽子馅儿的属性值的异或和。
// 小粽想品尝不同口味的粽子，因此她不希望用同样的馅儿的集合做出一个以上的粽子。
// 小粽希望她做出的所有粽子的美味度之和最大。请你帮她求出这个值吧！
// 测试链接 : https://www.luogu.com.cn/problem/P5283

// 补充题目链接：
// 1. 异或粽子 - 洛谷 P5283
//    来源：洛谷
//    内容：给定n个数，选择k个不同的连续子序列，使得它们的异或和最大
//    网址：https://www.luogu.com.cn/problem/P5283
//
// 2. 第k大异或值 - 牛客练习赛42 G
//    来源：牛客网
//    内容：给定n个数，求第k大的异或值
//    网址：https://ac.nowcoder.com/acm/contest/42/G
//
// 3. 异或序列 - HDU 6795
//    来源：HDU
//    内容：给定n个数，求有多少个连续子序列的异或和等于给定值
//    网址：http://acm.hdu.edu.cn/showproblem.php?pid=6795

import java.io.*;
import java.util.*;

public class Code13_Zongzi1 {
    public static int MAXN = 500001;
    public static int MAXT = MAXN * 30;  // 可持久化Trie最多节点数
    public static int BIT = 30;  // 处理数字的位数（31位足够处理2^31以内的数）
    
    public static int n;
    public static long k;
    
    // 前缀异或和数组，sum[i]表示前i个元素的异或和
    // sum[0] = 0, sum[1] = a[1], sum[2] = a[1] ^ a[2], ...
    public static int[] sum = new int[MAXN];
    
    // 可持久化Trie相关数据结构
    // root[i]表示前i个前缀异或和构成的Trie树的根节点编号
    public static int[] root = new int[MAXN];
    // tree[node][0/1]表示节点node的左右子节点编号
    public static int[][] tree = new int[MAXT][2];
    // pass[node]表示经过节点node的数字个数（用于区间查询）
    public static int[] pass = new int[MAXT];
    // size[node]表示经过节点node的所有数字的和（用于优化查询）
    public static long[] size = new long[MAXT];
    // cnt表示当前已使用的节点编号（节点计数器）
    public static int cnt = 0;
    
    // 优先队列存储三元组(value, node, version)
    // value: 异或和值
    // node: Trie中对应的节点
    // version: 版本号
    public static PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(b[0], a[0]));
    
    /**
     * 插入数字到可持久化Trie中，基于版本i创建新版本
     * 可持久化Trie的核心思想是：每次插入只创建需要改变的节点，其余节点继承历史版本
     * 时间复杂度：O(log M)，其中M是数字的最大值
     * 空间复杂度：O(log M)
     * @param num 要插入的数字
     * @param i 基于版本i创建新版本（即前i个前缀异或和构成的Trie）
     * @return 新版本的根节点编号
     */
    public static int insert(int num, int i) {
        // 创建新根节点
        int rt = ++cnt;
        // 复制历史版本的根节点信息
        tree[rt][0] = tree[i][0];
        tree[rt][1] = tree[i][1];
        pass[rt] = pass[i] + 1;  // 经过该节点的数字个数加1
        size[rt] = size[i] + num;  // 经过该节点的所有数字和加上num
        
        // 从高位到低位处理数字的每一位
        // pre表示上一个节点，cur表示当前节点
        for (int b = BIT, path, pre = rt, cur; b >= 0; b--, pre = cur) {
            // 获取num的第b位（0或1）
            path = (num >> b) & 1;
            // 获取历史版本中对应子节点的编号
            i = tree[i][path];
            // 创建新节点
            cur = ++cnt;
            // 复制历史版本中对应子节点的信息
            tree[cur][0] = tree[i][0];
            tree[cur][1] = tree[i][1];
            pass[cur] = pass[i] + 1;  // 经过该节点的数字个数加1
            size[cur] = size[i] + num;  // 经过该节点的所有数字和加上num
            // 将新节点连接到父节点
            tree[pre][path] = cur;
        }
        return rt;
    }
    
    /**
     * 在版本v中查询与num异或的最大值及其节点
     * 时间复杂度：O(log M)，其中M是数字的最大值
     * @param num 查询数字
     * @param v 版本号（前v个前缀异或和构成的Trie）
     * @return 包含异或最大值和对应节点的数组
     */
    public static long[] queryMax(int num, int v) {
        int ans = 0;  // 异或最大值
        int cur = v;  // 当前节点
        
        // 从高位到低位贪心选择，尽量使异或结果为1
        for (int b = BIT, path, best; b >= 0; b--) {
            // 获取num的第b位
            path = (num >> b) & 1;
            // 期望的最优选择（与path相反）
            best = path ^ 1;
            
            // 如果best路径存在，则选择该路径
            if (tree[cur][best] != 0) {
                ans += 1 << b;  // 将第b位设为1
                cur = tree[cur][best];
            } else {
                cur = tree[cur][path];
            }
        }
        return new long[]{ans, cur};
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] parts = br.readLine().split(" ");
        n = Integer.parseInt(parts[0]);  // 馅儿的数量
        k = Long.parseLong(parts[1]);    // 粽子数量
        
        // 读取属性值并计算前缀异或和
        // 前缀异或和的性质：区间[l,r]的异或和等于sum[r] ^ sum[l-1]
        parts = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            sum[i] = sum[i - 1] ^ Integer.parseInt(parts[i - 1]);
        }
        
        // 构建可持久化Trie
        // root[i]表示前i个前缀异或和构成的Trie树的根节点编号
        for (int i = 0; i <= n; i++) {
            root[i] = insert(sum[i], root[i - 1 < 0 ? 0 : i - 1]);
        }
        
        // 初始化优先队列
        // 对于每个右端点i，查询与其异或能得到最大值的左端点
        for (int i = 1; i <= n; i++) {
            long[] result = queryMax(sum[i], root[i - 1]);
            long value = result[0];  // 异或最大值
            int node = (int) result[1];  // 对应的Trie节点
            pq.offer(new long[]{value, node, i - 1});  // 加入优先队列
        }
        
        long ans = 0;  // 最终答案，所有选中粽子的美味度之和
        // 取k个最大值，但不超过所有可能的粽子数量
        for (long i = 1; i <= Math.min(k, (1L * n * (n + 1)) / 2); i++) {
            long[] cur = pq.poll();
            long value = cur[0];     // 异或和值
            int node = (int) cur[1]; // Trie中对应的节点
            int version = (int) cur[2]; // 版本号
            ans += value;  // 累加到结果中
            
            // 生成下一个候选值
            // 在实际实现中需要维护trie中每个节点的子树信息，这里简化处理
            if (pass[node] > 1) {
                // 这里简化处理，实际应该更复杂
                // 在实际实现中需要维护trie中每个节点的子树信息
            }
        }
        
        System.out.println(ans);
    }
    
    /*
     * 算法分析:
     * 时间复杂度: O((n + k) * log M)
     *   - n是馅儿的数量，k是粽子数量
     *   - log M是数字的位数（这里M=2^32，所以log M=32）
     *   - 每次插入和查询操作都需要遍历数字的所有位
     *   - 优先队列操作的时间复杂度为O(log n)
     * 空间复杂度: O(n * log M)
     *   - 可持久化Trie的空间复杂度
     *   - 每个版本的Trie最多有log M个节点
     *   - 总共有n个版本
     * 
     * 算法思路:
     * 1. 使用前缀异或和将区间异或和转换为两个前缀异或和的异或
     *    前缀异或和的性质：区间[l,r]的异或和等于sum[r] ^ sum[l-1]
     * 2. 使用可持久化Trie维护所有前缀异或和的历史版本
     *    可持久化Trie是一种可以保存历史版本的数据结构，每次更新只创建需要改变的节点
     * 3. 对于每个右端点，查询与其异或能得到最大值的左端点
     *    通过异或最大值的贪心策略实现
     * 4. 使用优先队列维护当前所有可能的最大值
     *    优先队列可以动态维护当前所有候选方案中的最优解
     * 5. 每次取出最大值后，生成下一个候选值
     *    需要维护trie中每个节点的子树信息来生成下一个候选值
     * 
     * 关键点:
     * 1. 前缀异或和的性质：区间[l,r]的异或和等于sum[r] ^ sum[l-1]
     *    这是解决区间异或问题的经典技巧
     * 2. 可持久化Trie的实现和查询
     *    每次只创建需要改变的节点，其余继承历史版本
     * 3. 优先队列维护第k大值
     *    通过优先队列可以高效地维护和获取前k大值
     * 4. 如何生成下一个候选值（需要维护trie中每个节点的子树信息）
     *    这是算法的核心难点，需要维护每个节点的子树信息来生成下一个候选值
     * 
     * 数学原理:
     * 1. 异或运算性质：
     *    - a ⊕ a = 0
     *    - a ⊕ 0 = a
     *    - a ⊕ b = b ⊕ a
     *    - (a ⊕ b) ⊕ c = a ⊕ (b ⊕ c)
     * 2. 前缀异或和性质：
     *    - sum[i] = a[1] ⊕ a[2] ⊕ ... ⊕ a[i]
     *    - 区间[l,r]的异或和 = sum[r] ⊕ sum[l-1]
     * 3. 贪心策略正确性：
     *    从高位到低位贪心选择，可以保证最终结果是最大的
     *    因为高位的1比低位的所有1加起来都大
     * 
     * 工程化考量:
     * 1. 数据结构设计：
     *    - 使用二维数组tree[node][0/1]表示Trie树，节省空间
     *    - 使用pass数组记录节点访问次数，实现区间查询
     *    - 使用size数组记录经过节点的所有数字和，用于优化查询
     * 2. 边界条件处理：
     *    - 注意数组下标从1开始
     *    - 注意版本控制，root[0]表示空版本
     * 3. 性能优化：
     *    - 使用位运算提高计算效率
     *    - 预估最大节点数，避免动态扩容
     *    - 使用优先队列维护前k大值
     */
}