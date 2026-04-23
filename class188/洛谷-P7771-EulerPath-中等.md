# 【洛谷】-P7771-欧拉路径-中等

## 题目原始链接
https://www.luogu.com.cn/problem/P7771

## 题目完整描述
给定一张 n 个点，m 条边的有向图，求一条字典序最小的欧拉路径。

### 输入格式
第一行两个整数 n,m，表示点数和边数。
接下来 m 行，每行两个整数 x,y，表示有一条从 x 到 y 的有向边。

### 输出格式
如果不存在欧拉路径，输出一行 "No"。
否则输出一行 m+1 个数，表示字典序最小的欧拉路径。

### 数据范围
- 1 ≤ n ≤ 10^5
- 1 ≤ m ≤ 2 × 10^5
- 1 ≤ x,y ≤ n

### 样例输入输出
```
输入：
3 3
1 2
2 3
3 1
输出：
1 2 3 1
```

## 笔试/面试考察点分析
- **考察点1**：有向图欧拉路径判定定理（入度与出度关系）
- **考察点2**：Hierholzer算法的迭代实现（避免递归栈溢出）
- **考察点3**：邻接表的高效构建与遍历
- **考察点4**：字典序最小路径的处理（预先排序边）
- **考察点5**：时间复杂度优化（O(m)而非O(m log m)）

## 解题思路
1. 首先判断是否存在欧拉路径：
   - 对于有向图，至多有一个点出度比入度多1（起点）
   - 至多有一个点入度比出度多1（终点）
   - 其他点入度等于出度
2. 如果是欧拉回路，则从任意有出边的点开始
3. 如果是欧拉路径，则从出度比入度多1的点开始
4. 使用Hierholzer算法找路径，为保证字典序最小，预先对边排序
5. 采用迭代方式实现，避免深度递归导致栈溢出

## 完整代码实现

```java
import java.io.*;
import java.util.*;

public class Main {
    static class EdgeCmp implements Comparator<int[]> {
        @Override
        public int compare(int[] e1, int[] e2) {
            // 按起点升序，起点相同时按终点升序
            return e1[0] != e2[0] ? (e1[0] - e2[0]) : (e1[1] - e2[1]);
        }
    }

    static final int MAXN = 100001;
    static final int MAXM = 200002;
    static int n, m;
    static int[][] edgeArr = new int[MAXM][2];

    static int[] head = new int[MAXN];
    static int[] nxt = new int[MAXM];
    static int[] to = new int[MAXM];
    static int cntg;

    static int[] cur = new int[MAXN];
    static int[] outDeg = new int[MAXN];
    static int[] inDeg = new int[MAXN];

    static int[] path = new int[MAXM];
    static int cntp;

    static void addEdge(int u, int v) {
        // 添加边到邻接表
        nxt[++cntg] = head[u];
        to[cntg] = v;
        head[u] = cntg;
    }

    static void connect() {
        // 构建邻接表，同时计算入度出度
        Arrays.sort(edgeArr, 1, m + 1, new EdgeCmp());
        for (int l = 1, r = 1; l <= m; l = ++r) {
            // 找到所有相同起点的边
            while (r + 1 <= m && edgeArr[l][0] == edgeArr[r + 1][0]) {
                r++;
            }
            // 逆序添加边，这样遍历时是按字典序的
            for (int i = r, u, v; i >= l; i--) {
                u = edgeArr[i][0];
                v = edgeArr[i][1];
                outDeg[u]++;
                inDeg[v]++;
                addEdge(u, v);
            }
        }
        // 初始化当前边指针
        for (int i = 1; i <= n; i++) {
            cur[i] = head[i];
        }
    }

    // 有向图中找到一个起点，去生成欧拉回路 或者 欧拉路径
    static int directedStart() {
        int start = -1, end = -1;
        for (int i = 1; i <= n; i++) {
            int v = outDeg[i] - inDeg[i];
            // 出度与入度差值只能是0或±1，且只能有一个起点和一个终点
            if (v < -1 || v > 1 || (v == 1 && start != -1) || (v == -1 && end != -1)) {
                return -1;
            }
            if (v == 1) {
                start = i;  // 出度比入度多1的点是起点
            }
            if (v == -1) {
                end = i;    // 入度比出度多1的点是终点
            }
        }
        // 要么都是-1（欧拉回路），要么都有值（欧拉路径）
        if ((start == -1) ^ (end == -1)) {
            return -1;
        }
        if (start != -1) {
            return start;  // 欧拉路径的起点
        }
        // 欧拉回路，从任意有出边的点开始
        for (int i = 1; i <= n; i++) {
            if (outDeg[i] > 0) {
                return i;
            }
        }
        return -1;
    }

    // Hierholzer算法迭代版，避免递归栈溢出
    static int[] sta = new int[MAXM];
    static int top;

    static void euler(int node) {
        top = 0;
        sta[++top] = node;
        while (top > 0) {
            int u = sta[top--];
            int e = cur[u];
            if (e != 0) {
                cur[u] = nxt[e];
                sta[++top] = u;
                sta[++top] = to[e];
            } else {
                path[++cntp] = u;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        FastReader in = new FastReader(System.in);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        n = in.nextInt();
        m = in.nextInt();
        for (int i = 1; i <= m; i++) {
            edgeArr[i][0] = in.nextInt();
            edgeArr[i][1] = in.nextInt();
        }
        connect();
        int start = directedStart();
        if (start == -1) {
            out.println("No");
        } else {
            euler(start);
            if (cntp != m + 1) {
                out.println("No");
            } else {
                for (int i = cntp; i >= 1; i--) {
                    out.print(path[i] + " ");
                }
                out.println();
            }
        }
        out.flush();
        out.close();
    }

    // 快速读入类，笔试中经常需要使用
    static class FastReader {
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0, len = 0;
        private final InputStream in;

        FastReader(InputStream in) {
            this.in = in;
        }

        private int readByte() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0)
                    return -1;
            }
            return buffer[ptr++];
        }

        int nextInt() throws IOException {
            int c;
            do {
                c = readByte();
            } while (c <= ' ' && c != -1);
            boolean neg = false;
            if (c == '-') {
                neg = true;
                c = readByte();
            }
            int val = 0;
            while (c > ' ' && c != -1) {
                val = val * 10 + (c - '0');
                c = readByte();
            }
            return neg ? -val : val;
        }
    }
}
```

## 代码逐行注释
- `static class EdgeCmp implements Comparator<int[]>` - 自定义比较器用于边的排序，确保字典序最小，笔试中需掌握自定义排序方法
- `addEdge(int u, int v)` - 链式前向星加边，面试需说明这种图存储方式的优点（空间效率高）
- `connect()` - 预处理阶段对边排序并建立邻接表，同时统计入度出度，笔试需注意预处理的重要性
- `directedStart()` - 有向图欧拉路径判定函数，核心是检查度数条件，面试高频考察点
- `euler(int node)` - Hierholzer算法迭代实现，避免递归深度过大导致栈溢出，笔试中大数据量必须使用迭代版
- `if (cntp != m + 1)` - 检查是否所有边都被遍历，验证欧拉路径的存在性，面试需说明这步验证的必要性
- **ML/DL关联**：该算法将有向图转换为节点序列，可用于图神经网络的节点访问序列

## 时间/空间复杂度分析
- **时间复杂度**：O(m log m)，主要消耗在边的排序上，如果是稠密图可以优化到O(m)
- **空间复杂度**：O(n + m)，用于存储图的邻接表和算法辅助数组
- **面试高频提问**：为什么使用迭代而非递归？因为当图深度较大时递归会导致栈溢出

## 同类题目拓展
- **类似题目1**：P2731 骑士巡游 - 无向图欧拉路径问题
- **类似题目2**：POJ 2230 - Watchcow - 无向图每边访问两次的问题
- **变种方向1**：如果要求找到所有可能的欧拉路径，如何修改算法？
- **变种方向2**：如果图不连通，如何检测并处理各个连通分量？

## ML/DL关联思考
- 该算法将图结构转化为节点序列，可作为RNN或Transformer的输入序列
- 在图神经网络中，欧拉路径提供了一种节点聚合顺序，影响信息传播效率
- 欧拉路径的全局遍历特性有助于捕获图的整体拓扑特征，提升GNN的表达能力
- 可以将欧拉路径的构造过程看作是一种图嵌入方法，将图映射到序列空间进行后续处理