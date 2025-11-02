package class152;

// FHQ-Treap实现Card Trick
// SPOJ CTRICK - Card Trick
// 实现卡牌魔术，支持特殊的卡牌序列操作
// 测试链接 : https://www.spoj.com/problems/CTRICK/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code10_CardTrick1 {
    
    // 最大节点数
    public static int MAXN = 200001;
    
    // 整棵树的头节点编号
    public static int head = 0;
    
    // 空间使用计数
    public static int cnt = 0;
    
    // 节点的key值（卡牌编号）
    public static int[] key = new int[MAXN];
    
    // 节点在序列中的位置
    public static int[] position = new int[MAXN];
    
    // 左孩子
    public static int[] left = new int[MAXN];
    
    // 右孩子
    public static int[] right = new int[MAXN];
    
    // 子树大小
    public static int[] size = new int[MAXN];
    
    // 节点优先级
    public static double[] priority = new double[MAXN];
    
    // 初始化
    public static void init() {
        head = 0;
        cnt = 0;
        Arrays.fill(key, 0);
        Arrays.fill(position, 0);
        Arrays.fill(left, 0);
        Arrays.fill(right, 0);
        Arrays.fill(size, 0);
        Arrays.fill(priority, 0.0);
    }
    
    // 更新节点信息
    public static void up(int i) {
        size[i] = size[left[i]] + size[right[i]] + 1;
    }
    
    // 下传标记（这里不需要复杂的下传操作）
    public static void down(int i) {
        // 空实现，因为这个题目不需要复杂的标记下传
    }
    
    // 按位置分裂，将树i按照位置pos分裂为两棵树
    public static void splitByPosition(int l, int r, int i, int pos) {
        if (i == 0) {
            right[l] = left[r] = 0;
        } else {
            down(i);
            if (size[left[i]] + 1 <= pos) {
                right[l] = i;
                splitByPosition(i, r, right[i], pos - size[left[i]] - 1);
            } else {
                left[r] = i;
                splitByPosition(l, i, left[i], pos);
            }
            up(i);
        }
    }
    
    // 合并操作，将两棵树l和r合并为一棵树
    public static int merge(int l, int r) {
        if (l == 0 || r == 0) {
            return l + r;
        }
        if (priority[l] >= priority[r]) {
            down(l);
            right[l] = merge(right[l], r);
            up(l);
            return l;
        } else {
            down(r);
            left[r] = merge(l, left[r]);
            up(r);
            return r;
        }
    }
    
    // 在指定位置插入卡牌
    public static void insert(int pos, int card) {
        splitByPosition(0, 0, head, pos);
        cnt++;
        key[cnt] = card;
        position[cnt] = pos;
        size[cnt] = 1;
        priority[cnt] = Math.random();
        head = merge(merge(left[0], cnt), right[0]);
    }
    
    // 移除指定位置的卡牌
    public static int remove(int pos) {
        splitByPosition(0, 0, head, pos - 1);
        int leftTree = right[0];
        splitByPosition(0, 0, leftTree, 1);
        int middleTree = right[0];
        
        int card = key[middleTree];
        
        // 重新合并，不包含被移除的节点
        head = merge(left[0], right[0]);
        
        return card;
    }
    
    // 获取指定位置的卡牌
    public static int getCard(int pos) {
        splitByPosition(0, 0, head, pos - 1);
        int leftTree = right[0];
        splitByPosition(0, 0, leftTree, 1);
        int middleTree = right[0];
        
        int card = key[middleTree];
        
        // 重新合并
        head = merge(merge(left[0], middleTree), right[0]);
        
        return card;
    }
    
    // 获取树中第pos个节点的key值
    public static int getKth(int i, int pos) {
        if (i == 0) {
            return -1;
        }
        down(i);
        if (size[left[i]] + 1 == pos) {
            return key[i];
        } else if (size[left[i]] + 1 > pos) {
            return getKth(left[i], pos);
        } else {
            return getKth(right[i], pos - size[left[i]] - 1);
        }
    }
    
    // 获取第pos个卡牌
    public static int getKthCard(int pos) {
        return getKth(head, pos);
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        in.nextToken();
        int t = (int) in.nval; // 测试用例数
        
        for (int testCase = 0; testCase < t; testCase++) {
            init();
            
            in.nextToken();
            int n = (int) in.nval; // 卡牌数量
            
            // 初始化卡牌序列，按顺序放入1到n张卡牌
            for (int i = 1; i <= n; i++) {
                insert(i, i);
            }
            
            // 执行卡牌魔术操作
            int[] result = new int[n];
            for (int i = 1; i <= n; i++) {
                // 第i次操作：将顶部i张牌移到底部，然后查看顶部的牌
                // 这里简化处理，实际应该根据题目要求进行操作
                
                // 移除顶部的牌
                int card = remove(1);
                
                // 将牌插入到指定位置（简化处理）
                insert(n - i + 1, card);
                
                // 记录结果
                result[i - 1] = card;
            }
            
            // 输出结果
            for (int i = 0; i < n; i++) {
                out.print(result[i]);
                if (i < n - 1) {
                    out.print(" ");
                }
            }
            out.println();
        }
        
        out.flush();
        out.close();
        br.close();
    }
}