package class159;

// 美味
// 一家餐厅有n道菜，编号1...n，大家对第i道菜的评价值为ai。
// 有m位顾客，第i位顾客的期望值为bi，而他的偏好值为xi。
// 因此，第i位顾客认为第j道菜的美味度为bi⊕(aj+xi)，⊕表示异或运算。
// 第i位顾客希望从这些菜中挑出他认为最美味的菜，即美味值最大的菜，
// 但由于价格等因素，他只能从第li道到第ri道中选择。
// 请你帮助他们找出最美味的菜。
// 测试链接 : https://www.luogu.com.cn/problem/P3293

// 补充题目链接：
// 1. 美味 - 洛谷 P3293
//    来源：洛谷
//    内容：给定n道菜的评价值，m个顾客查询，每个顾客在指定区间内找与(b+x)异或最大的评价值
//    网址：https://www.luogu.com.cn/problem/P3293
//
// 2. 最大异或对 - 洛谷 P4551
//    来源：洛谷
//    内容：给定n个数，找出两个数异或的最大值
//    网址：https://www.luogu.com.cn/problem/P4551
//
// 3. 区间异或最大值 - HDU 4825
//    来源：HDU
//    内容：给定n个数，m次查询，每次查询与给定数异或的最大值
//    网址：http://acm.hdu.edu.cn/showproblem.php?pid=4825

import java.io.*;
import java.util.*;

public class Code12_Delicious1 {
    public static int MAXN = 200001;
    public static int MAXT = MAXN * 22;  // 可持久化Trie最多节点数
    public static int BIT = 17;  // 处理数字的位数（18位足够处理10^5以内的数）
    
    public static int n, m;
    
    // 菜品评价值，arr[i]表示第i道菜的评价值
    public static int[] arr = new int[MAXN];
    
    // 可持久化Trie相关数据结构
    // root[i]表示前i道菜构成的Trie树的根节点编号
    public static int[] root = new int[MAXN];
    // tree[node][0/1]表示节点node的左右子节点编号
    public static int[][] tree = new int[MAXT][2];
    // pass[node]表示经过节点node的数字个数（用于区间查询）
    public static int[] pass = new int[MAXT];
    // cnt表示当前已使用的节点编号（节点计数器）
    public static int cnt = 0;
    
    /**
     * 插入数字到可持久化Trie中，基于版本i创建新版本
     * 可持久化Trie的核心思想是：每次插入只创建需要改变的节点，其余节点继承历史版本
     * 时间复杂度：O(log M)，其中M是数字的最大值
     * 空间复杂度：O(log M)
     * @param num 要插入的数字
     * @param i 基于版本i创建新版本（即前i道菜构成的Trie）
     * @return 新版本的根节点编号
     */
    public static int insert(int num, int i) {
        // 创建新根节点
        int rt = ++cnt;
        // 复制历史版本的根节点信息
        tree[rt][0] = tree[i][0];
        tree[rt][1] = tree[i][1];
        pass[rt] = pass[i] + 1;  // 经过该节点的数字个数加1
        
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
            // 将新节点连接到父节点
            tree[pre][path] = cur;
        }
        return rt;
    }
    
    /**
     * 在区间[u, v]版本中查询与num异或的最大值
     * 利用可持久化Trie实现区间查询，通过比较两个版本中节点pass值的差来判断区间内是否存在该路径
     * 时间复杂度：O(log M)，其中M是数字的最大值
     * @param num 查询数字
     * @param u 区间左端点版本（前u-1道菜构成的Trie）
     * @param v 区间右端点版本（前v道菜构成的Trie）
     * @return 与num异或的最大值
     */
    public static int query(int num, int u, int v) {
        int ans = 0;  // 最终结果
        
        // 从高位到低位贪心选择，尽量使异或结果为1
        for (int b = BIT, path, best; b >= 0; b--) {
            // 获取num的第b位
            path = (num >> b) & 1;
            // 期望的最优选择（与path相反）
            best = path ^ 1;
            
            // 判断在区间[u,v]中是否存在best路径
            // pass[tree[v][best]] - pass[tree[u][best]]表示区间内经过tree[v][best]但不经过tree[u][best]的数字个数
            if (pass[tree[v][best]] > pass[tree[u][best]]) {
                // 存在best路径，选择该路径
                ans += 1 << b;  // 将第b位设为1
                u = tree[u][best];
                v = tree[v][best];
            } else {
                // 不存在best路径，只能选择path路径
                u = tree[u][path];
                v = tree[v][path];
            }
        }
        return ans;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] parts = br.readLine().split(" ");
        n = Integer.parseInt(parts[0]);  // 菜品数量
        m = Integer.parseInt(parts[1]);  // 顾客数量
        
        // 读取菜品评价值
        parts = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(parts[i - 1]);
        }
        
        // 构建可持久化Trie
        // root[i]表示前i道菜构成的Trie树的根节点编号
        for (int i = 1; i <= n; i++) {
            root[i] = insert(arr[i], root[i - 1]);
        }
        
        // 处理顾客查询
        for (int i = 1; i <= m; i++) {
            parts = br.readLine().split(" ");
            int b = Integer.parseInt(parts[0]);  // 顾客期望值
            int x = Integer.parseInt(parts[1]);  // 顾客偏好值
            int l = Integer.parseInt(parts[2]);  // 可选菜品左端点
            int r = Integer.parseInt(parts[3]);  // 可选菜品右端点
            
            // 查询区间[l,r]中与(b+x)异或的最大值
            // root[l-1]表示前l-1道菜构成的Trie（不包含第l道菜）
            // root[r]表示前r道菜构成的Trie（包含第r道菜）
            out.println(query(b, root[l - 1], root[r]));
        }
        
        out.flush();
        out.close();
    }
    
    /*
     * 算法分析:
     * 时间复杂度: O((n + m) * log M)
     *   - n是菜品数，m是顾客数
     *   - log M是数字的位数（这里M=10^5，所以log M≈17）
     *   - 每次插入和查询操作都需要遍历数字的所有位
     * 空间复杂度: O(n * log M)
     *   - 每个版本的Trie最多有log M个节点
     *   - 总共有n个版本
     * 
     * 算法思路:
     * 1. 使用可持久化Trie维护所有菜品评价值的历史版本
     *    可持久化Trie是一种可以保存历史版本的数据结构，每次更新只创建需要改变的节点
     * 2. 对于每个查询，在指定区间版本中查找与(b+x)异或的最大值
     *    通过pass数组记录每个节点的出现次数，实现区间查询
     * 3. 通过pass数组记录每个节点的出现次数，实现区间查询
     *    区间[u,v]中经过某节点的数字个数 = pass[v] - pass[u]
     * 
     * 关键点:
     * 1. 可持久化Trie的实现：每次只创建需要改变的节点，其余继承历史版本
     *    这样可以大大节省空间，避免为每个版本都创建完整的Trie树
     * 2. 区间查询的实现：通过比较两个版本中节点pass值的差来判断区间内是否存在该路径
     *    这是可持久化数据结构的经典应用
     * 3. 异或最大值的贪心策略：从高位到低位，尽量选择与当前位相反的路径
     *    异或运算的性质：相同为0，不同为1，要使结果最大应尽量使高位为1
     * 
     * 数学原理:
     * 1. 异或运算性质：
     *    - a ⊕ a = 0
     *    - a ⊕ 0 = a
     *    - a ⊕ b = b ⊕ a
     *    - (a ⊕ b) ⊕ c = a ⊕ (b ⊕ c)
     * 2. 贪心策略正确性：
     *    从高位到低位贪心选择，可以保证最终结果是最大的
     *    因为高位的1比低位的所有1加起来都大
     * 
     * 工程化考量:
     * 1. 数据结构设计：
     *    - 使用二维数组tree[node][0/1]表示Trie树，节省空间
     *    - 使用pass数组记录节点访问次数，实现区间查询
     * 2. 边界条件处理：
     *    - 注意数组下标从1开始
     *    - 注意版本控制，root[0]表示空版本
     * 3. 性能优化：
     *    - 使用位运算提高计算效率
     *    - 预估最大节点数，避免动态扩容
     */
}