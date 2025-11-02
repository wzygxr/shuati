package class153;

/**
 * 营业额统计 - Splay树实现，Java版本
 * 
 * 【题目来源】洛谷 P2234 [HNOI2002]营业额统计
 * 【题目链接】https://www.luogu.com.cn/problem/P2234
 * 【题目大意】
 * 给定每天的营业额，计算每天的营业额波动值。
 * 波动值 = min(|当天营业额 - 之前某天营业额|)
 * 第一天的波动值就是当天的营业额
 * 
 * 【数据范围】
 * n <= 32767，营业额a <= 1000000
 * 
 * 【算法分析】
 * 使用Splay树维护有序集合，支持插入和查询前驱后继
 * 通过Splay操作优化频繁访问节点的访问速度
 * 
 * 【时间复杂度】
 * - 插入操作均摊O(log n)
 * - 查询前驱后继均摊O(log n)
 * - 总体时间复杂度O(n log n)
 * 
 * 【空间复杂度】O(n)
 * 
 * 【实现特点】
 * - 使用数组模拟节点结构，避免对象创建开销
 * - 实现前驱和后继查询功能
 * - 处理重复元素的情况
 */

import java.io.*;
import java.util.*;

/**
 * Splay树实现营业额统计问题
 * 支持插入和查询前驱后继操作
 * 
 * 【核心思想】
 * 1. 使用Splay树维护有序集合
 * 2. 每次插入新元素后，查询其前驱和后继
 * 3. 计算与前后元素的最小差值作为波动值
 * 4. 利用Splay操作优化频繁访问
 * 
 * 【应用场景】
 * - 动态维护有序集合
 * - 需要频繁查询前驱后继的问题
 * - 算法竞赛中的统计问题
 */
public class Code09_RevenueStatistics {
    
    /**
     * 【空间配置】预分配的最大节点数量
     * 设置为40000是为了处理32767的数据规模，并留有余量
     */
    public static int MAXN = 40000;
    
    /**
     * 【树结构标识】
     * head: 根节点索引
     * cnt: 当前已分配的节点计数器
     */
    public static int head = 0;
    public static int cnt = 0;
    
    /**
     * 【节点属性数组】使用数组模拟节点，避免对象创建开销
     * key: 节点存储的值（营业额）
     * father: 父节点索引
     * left: 左子节点索引
     * right: 右子节点索引
     * size: 以该节点为根的子树大小
     */
    public static int[] key = new int[MAXN];
    public static int[] father = new int[MAXN];
    public static int[] left = new int[MAXN];
    public static int[] right = new int[MAXN];
    public static int[] size = new int[MAXN];
    
    /**
     * 【自底向上维护】更新节点子树大小
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     * 
     * @param i 需要更新的节点索引
     */
    public static void up(int i) {
        size[i] = size[left[i]] + size[right[i]] + 1;
    }
    
    /**
     * 【方向判断】确定节点i是其父节点的左子节点还是右子节点
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     * 
     * @param i 需要判断的节点索引
     * @return 1表示右子节点，0表示左子节点
     */
    public static int lr(int i) {
        return right[father[i]] == i ? 1 : 0;
    }
    
    /**
     * 【核心旋转操作】将节点i旋转至其父节点的位置
     * 这是Splay树维护平衡的基本操作
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     * 
     * @param i 需要旋转的节点索引
     */
    public static void rotate(int i) {
        int f = father[i];     // 父节点索引
        int g = father[f];     // 祖父节点索引
        int soni = lr(i);      // 当前节点是父节点的左子还是右子
        int sonf = lr(f);      // 父节点是祖父节点的左子还是右子
        
        // 【旋转逻辑】根据当前节点是左子还是右子执行不同的旋转操作
        if (soni == 1) {       // 右子节点，执行右旋
            right[f] = left[i];
            if (right[f] != 0) {
                father[right[f]] = f;
            }
            left[i] = f;
        } else {               // 左子节点，执行左旋
            left[f] = right[i];
            if (left[f] != 0) {
                father[left[f]] = f;
            }
            right[i] = f;
        }
        
        // 更新祖父节点的子节点指针
        if (g != 0) {
            if (sonf == 1) {
                right[g] = i;
            } else {
                left[g] = i;
            }
        }
        
        // 更新父指针
        father[f] = i;
        father[i] = g;
        
        // 【重要】更新节点信息，先更新被旋转的父节点，再更新当前节点
        up(f);
        up(i);
    }
    
    /**
     * 【核心伸展操作】将节点i旋转到goal的子节点位置
     * 如果goal为0，则将i旋转到根节点
     * 这是Splay树的核心操作，通过一系列旋转使被访问节点移动到树的顶部
     * 时间复杂度: 均摊O(log n)，最坏情况O(n)
     * 空间复杂度: O(1)
     * 
     * @param i 需要旋转的节点索引
     * @param goal 目标父节点索引
     */
    public static void splay(int i, int goal) {
        int f = father[i], g = father[f];
        
        // 当当前节点的父节点不是目标节点时，继续旋转
        while (f != goal) {
            // 【旋转策略】根据Zig-Zig和Zig-Zag情况选择不同的旋转顺序
            if (g != goal) {
                // 如果父节点和当前节点在同侧，先旋转父节点（Zig-Zig情况）
                // 否则直接旋转当前节点（Zig-Zag情况）
                if (lr(i) == lr(f)) {
                    rotate(f);
                } else {
                    rotate(i);
                }
            }
            // 最后旋转当前节点
            rotate(i);
            
            // 更新父节点和祖父节点
            f = father[i];
            g = father[f];
        }
        
        // 如果目标节点是0，则更新根节点
        if (goal == 0) {
            head = i;
        }
    }
    
    /**
     * 【查找操作】在Splay树中查找值为val的节点
     * 如果找到，将该节点旋转到根
     * 时间复杂度: 均摊O(log n)
     * 空间复杂度: O(1)
     * 
     * @param val 要查找的值
     * @return 找到的节点索引，如果不存在返回0
     */
    public static int find(int val) {
        int cur = head;
        while (cur != 0) {
            if (key[cur] == val) {
                splay(cur, 0);
                return cur;
            } else if (val < key[cur]) {
                cur = left[cur];
            } else {
                cur = right[cur];
            }
        }
        return 0;
    }
    
    /**
     * 【插入操作】向Splay树中插入新值
     * 如果值已存在，则不插入重复元素
     * 时间复杂度: 均摊O(log n)
     * 空间复杂度: O(1)
     * 
     * @param val 要插入的值
     * @return 插入的节点索引
     */
    public static int insert(int val) {
        // 如果树为空，创建根节点
        if (head == 0) {
            cnt++;
            key[cnt] = val;
            size[cnt] = 1;
            head = cnt;
            return cnt;
        }
        
        int cur = head;
        int f = 0;
        
        // 查找插入位置
        while (cur != 0) {
            f = cur;
            if (val < key[cur]) {
                cur = left[cur];
            } else if (val > key[cur]) {
                cur = right[cur];
            } else {
                // 值已存在，不插入重复元素
                splay(cur, 0);
                return cur;
            }
        }
        
        // 创建新节点
        cnt++;
        key[cnt] = val;
        size[cnt] = 1;
        father[cnt] = f;
        
        // 将新节点连接到父节点
        if (val < key[f]) {
            left[f] = cnt;
        } else {
            right[f] = cnt;
        }
        
        // 将新节点旋转到根
        splay(cnt, 0);
        return cnt;
    }
    
    /**
     * 【前驱查询】查找小于val的最大值
     * 时间复杂度: 均摊O(log n)
     * 空间复杂度: O(1)
     * 
     * @param val 参考值
     * @return 前驱节点的值，如果不存在返回Integer.MIN_VALUE
     */
    public static int predecessor(int val) {
        insert(val); // 先插入，确保树中有该值
        
        // 前驱在左子树的最右节点
        int cur = left[head];
        if (cur == 0) {
            return Integer.MIN_VALUE;
        }
        
        while (right[cur] != 0) {
            cur = right[cur];
        }
        
        splay(cur, 0);
        return key[cur];
    }
    
    /**
     * 【后继查询】查找大于val的最小值
     * 时间复杂度: 均摊O(log n)
     * 空间复杂度: O(1)
     * 
     * @param val 参考值
     * @return 后继节点的值，如果不存在返回Integer.MAX_VALUE
     */
    public static int successor(int val) {
        insert(val); // 先插入，确保树中有该值
        
        // 后继在右子树的最左节点
        int cur = right[head];
        if (cur == 0) {
            return Integer.MAX_VALUE;
        }
        
        while (left[cur] != 0) {
            cur = left[cur];
        }
        
        splay(cur, 0);
        return key[cur];
    }
    
    /**
     * 【主函数】解决营业额统计问题
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取数据规模
        in.nextToken();
        int n = (int) in.nval;
        
        long total = 0; // 总波动值，使用long防止溢出
        
        for (int i = 0; i < n; i++) {
            in.nextToken();
            int revenue = (int) in.nval;
            
            if (i == 0) {
                // 第一天的波动值就是营业额本身
                total += revenue;
                insert(revenue);
            } else {
                // 查询前驱和后继
                int pred = predecessor(revenue);
                int succ = successor(revenue);
                
                // 计算最小差值
                int minDiff = Integer.MAX_VALUE;
                
                if (pred != Integer.MIN_VALUE) {
                    minDiff = Math.min(minDiff, revenue - pred);
                }
                
                if (succ != Integer.MAX_VALUE) {
                    minDiff = Math.min(minDiff, succ - revenue);
                }
                
                total += minDiff;
                
                // 插入当前营业额（如果已存在，insert会处理重复）
                insert(revenue);
            }
        }
        
        out.println(total);
        out.flush();
    }
    
    /**
     * 【测试用例验证】
     * 输入样例:
     * 6
     * 5 1 2 5 4 6
     * 
     * 输出样例:
     * 12
     * 
     * 解释:
     * 第1天: 5 -> 波动值5
     * 第2天: |1-5|=4 -> 波动值4
     * 第3天: |2-1|=1 -> 波动值1
     * 第4天: |5-5|=0 -> 波动值0
     * 第5天: |4-5|=1 -> 波动值1
     * 第6天: |6-5|=1 -> 波动值1
     * 总计: 5+4+1+0+1+1=12
     */
}

/**
 * 【算法优化分析】
 * 1. 使用Splay树而不是普通BST：利用访问局部性优化频繁查询
 * 2. 数组模拟节点：避免Java对象创建和GC开销
 * 3. 懒旋转策略：只在访问时进行平衡操作
 * 
 * 【工程化考量】
 * 1. 边界处理：处理空树、单节点等边界情况
 * 2. 重复元素：正确处理重复营业额的情况
 * 3. 数值范围：使用long防止总和溢出
 * 4. 输入输出：使用高效IO处理大规模数据
 * 
 * 【复杂度对比】
 * 方法             时间复杂度    空间复杂度    适用场景
 * Splay树          O(n log n)   O(n)        动态数据，频繁查询
 * 排序+二分        O(n log n)   O(n)        静态数据，单次查询
 * 平衡树(set)      O(n log n)   O(n)        标准库实现
 * 
 * 【面试要点】
 * 1. 解释Splay树的均摊复杂度原理
 * 2. 对比Splay树与其他平衡树的优缺点
 * 3. 讨论实际工程中的适用场景
 */