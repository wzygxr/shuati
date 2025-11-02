package class159;

// 可持久化异或最大值
// 给定一个非负整数序列，初始长度为N
// 有M个操作，操作类型如下：
// 1. A x：在序列末尾添加数字x
// 2. Q l r x：在位置l到r中找到一个位置p，使得 a[p] XOR a[p+1] XOR ... XOR a[N] XOR x 最大
// 测试链接 : https://www.luogu.com.cn/problem/P4735

// 补充题目1: 最大异或对
// 给定一个非负整数数组 nums，返回 nums[i] XOR nums[j] 的最大结果，其中 0 <= i <= j < n
// 测试链接: https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/
// 测试链接: https://www.luogu.com.cn/problem/P4551
// 相关题目:
// - https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/
// - https://www.luogu.com.cn/problem/P4551
// - https://www.hdu.edu.cn/problem/4825
// - https://codeforces.com/problemset/problem/282/E

// 补充题目2: 区间异或最大值查询
// 支持在线添加数字和区间异或最大值查询
// 测试链接: https://www.luogu.com.cn/problem/P4735
// 相关题目:
// - https://www.luogu.com.cn/problem/P4735
// - https://www.luogu.com.cn/problem/P4592
// - https://codeforces.com/problemset/problem/1175/G

// 补充题目3: 树上异或路径最大值
// 树上子树和路径的异或最大值查询
// 测试链接: https://www.luogu.com.cn/problem/P4592
// 相关题目:
// - https://www.luogu.com.cn/problem/P4592
// - https://www.hdu.edu.cn/problem/4757
// - https://codeforces.com/problemset/problem/1175/G

import java.io.*;
import java.util.*;

public class Code09_PersistentXor {
    // 最大节点数，根据题目数据范围设置
    public static int MAXN = 600001;
    
    // Trie树最大节点数，每个数字最多需要26位（BIT+1）
    public static int MAXT = MAXN * 22;
    
    // 位数，由于数字范围是0 <= arr[i], x <= 10^7，所以最多需要24位（2^24 > 10^7）
    public static int BIT = 25;
    
    // 当前数组长度和操作数
    public static int n, m;
    
    // 当前前缀异或和，用于维护数组的异或前缀和
    public static int eor;
    
    // 可持久化Trie相关数据结构
    
    // root[i]表示前i个数构成的可持久化Trie树的根节点编号
    // 用于维护历史版本信息，支持区间查询
    public static int[] root = new int[MAXN];
    
    // tree[i][0/1]表示Trie树节点i的左右子节点编号
    // 0表示bit=0的子节点，1表示bit=1的子节点
    public static int[][] tree = new int[MAXT][2];
    
    // pass[i]表示经过Trie树节点i的数字个数
    // 用于区间查询：区间[u,v]中某路径的数字个数 = pass[v] - pass[u]
    public static int[] pass = new int[MAXT];
    
    // 当前使用的Trie树节点编号计数器
    public static int cnt = 0;
    
    /**
     * 在可持久化Trie树中插入一个数字
     * 实现可持久化的核心：只创建被修改的节点，其余节点继承历史版本
     * 
     * @param num 要插入的数字（前缀异或和）
     * @param i 前一个版本的根节点编号
     * @return 新版本的根节点编号
     */
    public static int insert(int num, int i) {
        // 创建新根节点
        int rt = ++cnt;
        // 复用前一个版本的左右子树（可持久化的核心）
        tree[rt][0] = tree[i][0];
        tree[rt][1] = tree[i][1];
        // 经过该节点的数字个数加1
        pass[rt] = pass[i] + 1;
        
        // 从高位到低位处理数字的每一位（Trie树的构建过程）
        for (int b = BIT, path, pre = rt, cur; b >= 0; b--, pre = cur) {
            // 提取第b位的值（0或1）
            path = (num >> b) & 1;
            // 获取前一个版本中对应子节点
            i = tree[i][path];
            // 创建新节点（只创建需要改变的节点）
            cur = ++cnt;
            // 复用前一个版本的子节点信息
            tree[cur][0] = tree[i][0];
            tree[cur][1] = tree[i][1];
            // 更新经过该节点的数字个数
            pass[cur] = pass[i] + 1;
            // 连接父子节点
            tree[pre][path] = cur;
        }
        return rt;
    }
    
    /**
     * 在可持久化Trie树中查询区间[u,v]与num异或的最大值
     * 利用pass数组实现区间查询：通过比较两个版本中节点pass值的差来判断区间内是否存在该路径
     * 
     * @param num 查询的目标数字
     * @param u 区间左边界对应版本的根节点编号
     * @param v 区间右边界对应版本的根节点编号
     * @return 区间内与num异或的最大值
     */
    public static int query(int num, int u, int v) {
        int ans = 0;
        // 从高位到低位贪心选择使异或结果最大的路径
        for (int b = BIT, path, best; b >= 0; b--) {
            // 提取第b位的值
            path = (num >> b) & 1;
            // 贪心策略：尽量选择与当前位相反的路径（使异或结果最大）
            best = path ^ 1;
            
            // 区间查询的关键：通过pass值差判断区间内是否存在best路径
            // 如果在区间[u,v]中存在best路径，则选择该路径
            if (pass[tree[v][best]] > pass[tree[u][best]]) {
                // 将第b位置为1（异或结果为1）
                ans += 1 << b;
                // 移动到best子节点
                u = tree[u][best];
                v = tree[v][best];
            } else {
                // 否则只能选择相同路径
                u = tree[u][path];
                v = tree[v][path];
            }
        }
        return ans;
    }
    
    public static void main(String[] args) throws IOException {
        // 使用BufferedReader和PrintWriter提高IO效率（竞赛编程常用技巧）
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取第一行：n（初始数组长度）和m（操作数）
        String[] parts = br.readLine().split(" ");
        n = Integer.parseInt(parts[0]);
        m = Integer.parseInt(parts[1]);
        
        // 初始化前缀异或和为0
        eor = 0;
        // 插入前缀异或和0，表示空数组的情况（边界处理）
        root[0] = insert(eor, 0);
        
        // 读取初始数组并构建前缀异或和Trie
        parts = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            // 读取第i个数字
            int num = Integer.parseInt(parts[i - 1]);
            // 更新前缀异或和
            eor ^= num;
            // 插入前缀异或和并更新根节点
            root[i] = insert(eor, root[i - 1]);
        }
        
        // 处理M个操作
        for (int i = 1; i <= m; i++) {
            parts = br.readLine().split(" ");
            // 判断操作类型
            if (parts[0].equals("A")) {
                // 添加操作：A x
                int x = Integer.parseInt(parts[1]);
                // 更新前缀异或和
                eor ^= x;
                // 数组长度增加
                n++;
                // 插入新的前缀异或和并更新根节点
                root[n] = insert(eor, root[n - 1]);
            } else {
                // 查询操作：Q l r x
                int l = Integer.parseInt(parts[1]);  // 区间左边界
                int r = Integer.parseInt(parts[2]);  // 区间右边界
                int x = Integer.parseInt(parts[3]);  // 查询目标值
                
                // 根据异或前缀和的性质进行转换：
                // a[p] XOR a[p+1] XOR ... XOR a[N] XOR x 
                // = prefix[p-1] XOR prefix[N] XOR x
                // 其中prefix[i]表示前i个数的异或和
                if (l == 1) {
                    // 查询整个区间[1,r]：prefix[0] XOR prefix[N] XOR x
                    // 由于prefix[0]=0，所以结果为prefix[N] XOR x
                    out.println(query(eor ^ x, 0, root[r - 1]));
                } else {
                    // 查询区间[l,r]：prefix[l-1] XOR prefix[N] XOR x
                    out.println(query(eor ^ x, root[l - 2], root[r - 1]));
                }
            }
        }
        
        // 刷新输出缓冲区并关闭
        out.flush();
        out.close();
    }
    
    /*
     * 算法分析:
     * 时间复杂度: O((n + m) * log M)
     *   - n是初始数组长度，m是操作数
     *   - log M是数字的位数（这里M=10^7，所以log M≈24）
     *   - 每次插入和查询操作都需要遍历数字的所有位
     * 空间复杂度: O(n * log M)
     *   - 每个版本的Trie最多有log M个节点
     *   - 总共有n个版本
     * 
     * 算法思路:
     * 1. 利用异或前缀和的性质：a[p] XOR a[p+1] XOR ... XOR a[N] = prefix[p-1] XOR prefix[N]
     * 2. 使用可持久化Trie维护所有前缀异或和的历史版本
     * 3. 对于查询操作，转换为在指定区间版本中查找与固定值异或的最大值
     * 4. 通过pass数组记录每个节点的出现次数，实现区间查询
     * 
     * 关键点:
     * 1. 可持久化Trie的实现：每次只创建需要改变的节点，其余继承历史版本
     * 2. 区间查询的实现：通过比较两个版本中节点pass值的差来判断区间内是否存在该路径
     * 3. 异或前缀和的转换：将区间异或查询转换为两个前缀异或值的异或
     * 
     * 数学原理:
     * 异或前缀和性质：对于数组a[1..n]，定义prefix[i] = a[1] XOR a[2] XOR ... XOR a[i]
     * 则 a[l] XOR a[l+1] XOR ... XOR a[r] = prefix[l-1] XOR prefix[r]
     * 
     * 工程化考量:
     * 1. IO优化：使用BufferedReader和PrintWriter提高读写效率
     * 2. 内存管理：合理设置数组大小，避免内存浪费
     * 3. 边界处理：正确处理空数组、单元素等特殊情况
     * 4. 性能优化：使用位运算提高计算效率
     * 
     * 跨语言实现差异:
     * 1. Java有自动垃圾回收，不需要手动释放内存
     * 2. Java中的数组访问需要边界检查，可能比C++稍慢
     * 3. Java中的IO操作可以通过BufferedReader/PrintWriter优化
     * 
     * 算法在工程中的应用:
     * 1. 数据库索引：可持久化数据结构可用于实现高效的数据库索引
     * 2. 版本控制系统：类似Git的版本控制可以通过可持久化数据结构实现
     * 3. 实时推荐系统：可持久化Trie可用于维护用户行为历史并进行实时查询
     * 4. 网络路由：Trie树结构广泛应用于网络路由表的实现
     * 
     * 调试技巧:
     * 1. 打印中间变量：在关键步骤打印Trie节点状态和pass值
     * 2. 小例子测试：用简单的测试用例验证算法逻辑的正确性
     * 3. 边界测试：测试空数组、单元素数组等特殊情况
     * 4. 性能分析：对于大数据量输入，使用性能分析工具监控时间和内存占用
     * 
     * 算法优化建议:
     * 1. 对于稀疏数据，可以使用压缩Trie减少空间占用
     * 2. 对于频繁查询的场景，可以增加缓存机制
     * 3. 可以使用位运算的优化技巧，如预计算位掩码
     * 4. 对于多线程环境，可以考虑使用无锁数据结构
     */
}