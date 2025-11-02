package class151;

// POJ 2201 Cartesian Tree
// 给定 n 对 (key, value)，构建笛卡尔树，满足 key 满足二叉搜索树性质，value 满足堆性质
// 测试链接 : http://poj.org/problem?id=2201
// 提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class POJ2201_CartesianTree {

    // 最大节点数
    public static int MAXN = 50001;

    // 节点信息
    public static int[] key = new int[MAXN];      // key值，满足二叉搜索树性质
    public static int[] value = new int[MAXN];    // value值，满足堆性质
    public static int[] parent = new int[MAXN];   // 父节点
    public static int[] left = new int[MAXN];     // 左子节点
    public static int[] right = new int[MAXN];    // 右子节点
    
    // 用于排序的索引数组
    public static Integer[] indices = new Integer[MAXN];
    
    // 单调栈
    public static int[] stack = new int[MAXN];

    public static int n;

    /**
     * 构建笛卡尔树
     * 核心思想：
     * 1. 按key值对节点进行排序
     * 2. 使用单调栈按value值构建满足堆性质的树结构
     */
    public static void buildCartesianTree() {
        // 初始化所有节点的父节点和子节点为0（空节点）
        Arrays.fill(parent, 0);
        Arrays.fill(left, 0);
        Arrays.fill(right, 0);
        
        // 初始化索引数组
        for (int i = 1; i <= n; i++) {
            indices[i] = i;
        }
        
        // 按key值对索引数组进行排序，保证二叉搜索树性质
        Arrays.sort(indices, 1, n + 1, (a, b) -> key[a] - key[b]);
        
        // 使用单调栈构建笛卡尔树（按value值构建堆性质）
        int top = 0;  // 栈顶指针
        for (int i = 1; i <= n; i++) {
            // 获取排序后的节点索引
            int idx = indices[i];
            int pos = top;
            
            // 维护单调栈，弹出value值大于当前节点的节点
            // 保证栈中节点的value按从小到大排列（小根堆性质）
            while (pos > 0 && value[stack[pos]] > value[idx]) {
                pos--;
            }
            
            // 建立父子关系
            if (pos > 0) {
                // 栈顶元素作为当前元素的父节点，当前元素作为其右子节点
                parent[idx] = stack[pos];
                right[stack[pos]] = idx;
            }
            
            if (pos < top) {
                // 当前节点的左子节点是最后被弹出的节点
                parent[stack[pos + 1]] = idx;
                left[idx] = stack[pos + 1];
            }
            
            // 将当前节点压入栈中
            stack[++pos] = idx;
            // 更新栈顶指针
            top = pos;
        }
    }

    /**
     * 主函数，处理输入输出
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        in.nextToken();
        n = (int) in.nval;
        
        // 读取节点的key和value值
        for (int i = 1; i <= n; i++) {
            in.nextToken();
            key[i] = (int) in.nval;
            in.nextToken();
            value[i] = (int) in.nval;
        }
        
        // 构建笛卡尔树
        buildCartesianTree();
        
        // 输出结果
        out.println("YES");
        for (int i = 1; i <= n; i++) {
            // 输出每个节点的父节点、左子节点、右子节点
            out.println(parent[i] + " " + left[i] + " " + right[i]);
        }
        
        out.flush();
        out.close();
        br.close();
    }

}