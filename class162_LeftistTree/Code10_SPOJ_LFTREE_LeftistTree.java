package class154;

/**
 * SPOJ LFTREE Leftist Tree
 * 题目链接: https://www.spoj.com/problems/LFTREE/
 * 
 * 题目大意:
 * 实现左偏树的基本操作，包括合并和删除最小元素操作
 * 
 * 算法思路:
 * 使用左偏树实现可合并堆，支持高效的合并操作和删除最小元素操作
 * 
 * 时间复杂度:
 * - 合并操作: O(log n)
 * - 删除最小元素: O(log n)
 * - 插入元素: O(log n)
 * 
 * 空间复杂度: O(n)
 */
import java.util.*;
import java.io.*;

public class Code10_SPOJ_LFTREE_LeftistTree {
    // 最大节点数
    static final int MAXN = 100001;
    
    // 节点值数组
    static int[] value = new int[MAXN];
    
    // 左右子节点数组
    static int[] left = new int[MAXN];
    static int[] right = new int[MAXN];
    
    // 距离数组
    static int[] dist = new int[MAXN];
    
    // 并查集数组
    static int[] father = new int[MAXN];
    
    /**
     * 初始化函数
     * @param n 节点数量
     */
    static void prepare(int n) {
        // 空节点的距离定义为-1
        dist[0] = -1;
        
        // 初始化每个节点
        for (int i = 1; i <= n; i++) {
            left[i] = right[i] = 0;
            dist[i] = 0;
            father[i] = i;
        }
    }
    
    /**
     * 并查集查找函数，带路径压缩优化
     * @param i 节点编号
     * @return 节点所在集合的代表元素
     */
    static int find(int i) {
        return father[i] = (father[i] == i) ? i : find(father[i]);
    }
    
    /**
     * 合并两棵左偏树，维护小根堆性质
     * @param i 第一棵左偏树的根节点编号
     * @param j 第二棵左偏树的根节点编号
     * @return 合并后新树的根节点编号
     */
    static int merge(int i, int j) {
        // 递归终止条件
        if (i == 0 || j == 0) {
            return i + j;
        }
        
        // 维护小根堆性质
        if (value[i] > value[j]) {
            int tmp = i;
            i = j;
            j = tmp;
        }
        
        // 递归合并右子树和j
        right[i] = merge(right[i], j);
        
        // 维护左偏性质
        if (dist[left[i]] < dist[right[i]]) {
            int tmp = left[i];
            left[i] = right[i];
            right[i] = tmp;
        }
        
        // 更新距离
        dist[i] = dist[right[i]] + 1;
        
        // 更新父节点信息
        father[left[i]] = father[right[i]] = i;
        
        return i;
    }
    
    /**
     * 删除堆顶元素（最小值）
     * @param i 堆顶节点编号
     * @return 删除堆顶后新树的根节点编号
     */
    static int pop(int i) {
        // 将左右子节点的father设置为自己
        father[left[i]] = left[i];
        father[right[i]] = right[i];
        
        // 合并左右子树
        father[i] = merge(left[i], right[i]);
        
        // 清空当前节点信息
        left[i] = right[i] = dist[i] = 0;
        
        return father[i];
    }
    
    /**
     * 主函数
     * @param args 命令行参数
     */
    public static void main(String[] args) throws IOException {
        // 由于SPOJ的输入输出格式限制，这里使用简化版本
        // 实际在SPOJ上需要使用特定的输入输出方式
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        
        while ((line = reader.readLine()) != null) {
            StringTokenizer tokenizer = new StringTokenizer(line);
            int n = Integer.parseInt(tokenizer.nextToken());
            int m = Integer.parseInt(tokenizer.nextToken());
            
            if (n == 0 && m == 0) {
                break;
            }
            
            // 初始化
            prepare(n);
            
            // 读取每个节点的初始值
            tokenizer = new StringTokenizer(reader.readLine());
            for (int i = 1; i <= n; i++) {
                value[i] = Integer.parseInt(tokenizer.nextToken());
            }
            
            // 处理操作
            for (int i = 0; i < m; i++) {
                tokenizer = new StringTokenizer(reader.readLine());
                int op = Integer.parseInt(tokenizer.nextToken());
                
                if (op == 1) {
                    // 合并操作
                    int x = Integer.parseInt(tokenizer.nextToken());
                    int y = Integer.parseInt(tokenizer.nextToken());
                    
                    int rootX = find(x);
                    int rootY = find(y);
                    
                    if (rootX != rootY) {
                        int newRoot = merge(rootX, rootY);
                        father[newRoot] = newRoot;
                    }
                } else {
                    // 删除最小元素操作
                    int x = Integer.parseInt(tokenizer.nextToken());
                    int root = find(x);
                    System.out.println(value[root]);
                    pop(root);
                }
            }
        }
    }
}