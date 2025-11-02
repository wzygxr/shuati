# 分块查找算法专题

## 算法原理

分块查找（Blocking Search），也称为索引顺序查找，是一种结合了顺序查找和二分查找优点的查找算法。它的基本思想是：

1. 将待查找的线性表分成若干块（子表）
2. 块内元素可以是无序的，但块之间必须是有序的
3. 建立一个索引表，记录每块的起始位置和块内的最大（或最小）值
4. 查找时，先通过索引表确定目标值可能在哪个块中
5. 然后在确定的块内进行顺序查找

这种方法既保留了顺序查找对线性表无序或部分无序的适应性，又通过分块和索引提高了查找效率。

## 时间复杂度分析

- 索引表的查找时间：如果索引表是有序的，可以使用二分查找，时间复杂度为 O(log m)，其中 m 是块的数量
- 块内查找时间：采用顺序查找，平均时间复杂度为 O(n/m)，其中 n 是数据总量
- 总体平均时间复杂度：O(log m + n/m)
- 当 m = √n 时，总体时间复杂度达到最优，为 O(√n)

## 算法实现

### 1. 基本分块查找实现

**题目来源**：数据结构基础题

**题目描述**：实现分块查找算法，包括数据分块、索引表构建和查找过程

**解题思路**：
- 首先将数据分成大小相近的若干块
- 构建索引表，记录每块的起始位置和最大值
- 实现查找函数，先在索引表中查找可能包含目标值的块
- 然后在该块内进行顺序查找

**代码实现**：

**Java代码**：
```java
import java.util.Arrays;

public class BlockSearch {
    // 索引表：每个元素存储块的起始位置和最大值
    static class Index {
        int start;  // 块的起始位置
        int max;    // 块内最大值
        
        Index(int start, int max) {
            this.start = start;
            this.max = max;
        }
    }
    
    private int[] data;       // 原始数据数组
    private Index[] index;    // 索引表
    private int blockSize;    // 块的大小
    private int blockCount;   // 块的数量
    
    // 构造函数，初始化分块查找结构
    public BlockSearch(int[] array) {
        this.data = Arrays.copyOf(array, array.length);
        // 计算块的大小，通常取数据量的平方根
        this.blockSize = (int) Math.sqrt(array.length);
        // 计算块的数量
        this.blockCount = (array.length + blockSize - 1) / blockSize;  // 向上取整
        
        // 构建索引表
        buildIndex();
    }
    
    // 构建索引表
    private void buildIndex() {
        index = new Index[blockCount];
        
        for (int i = 0; i < blockCount; i++) {
            int start = i * blockSize;
            int end = Math.min(start + blockSize, data.length);
            
            // 找出块内最大值
            int max = data[start];
            for (int j = start + 1; j < end; j++) {
                if (data[j] > max) {
                    max = data[j];
                }
            }
            
            index[i] = new Index(start, max);
        }
    }
    
    // 查找函数
    public int search(int target) {
        // 1. 在索引表中查找目标值可能在的块
        int blockIndex = -1;
        for (int i = 0; i < blockCount; i++) {
            if (target <= index[i].max) {
                blockIndex = i;
                break;
            }
        }
        
        // 目标值大于所有块的最大值，不存在
        if (blockIndex == -1) {
            return -1;
        }
        
        // 2. 在对应的块内进行顺序查找
        int start = index[blockIndex].start;
        int end = Math.min(start + blockSize, data.length);
        
        for (int i = start; i < end; i++) {
            if (data[i] == target) {
                return i;  // 返回目标值在原始数组中的索引
            }
        }
        
        return -1;  // 目标值不存在
    }
    
    // 打印分块信息
    public void printBlocks() {
        System.out.println("分块信息：");
        for (int i = 0; i < blockCount; i++) {
            int start = index[i].start;
            int end = Math.min(start + blockSize, data.length);
            System.out.print("块" + (i + 1) + " [" + start + "-" + (end - 1) + "]: ");
            for (int j = start; j < end; j++) {
                System.out.print(data[j] + " ");
            }
            System.out.println(" (最大值: " + index[i].max + ")");
        }
    }
    
    public static void main(String[] args) {
        int[] array = {22, 12, 13, 8, 9, 20, 33, 42, 44, 38, 24, 48, 60, 58, 74, 49, 86, 53};
        BlockSearch searcher = new BlockSearch(array);
        
        // 打印分块信息
        searcher.printBlocks();
        
        // 测试查找
        int[] targets = {13, 42, 50, 86};
        for (int target : targets) {
            int index = searcher.search(target);
            if (index != -1) {
                System.out.println("查找 " + target + " 成功，索引位置：" + index);
            } else {
                System.out.println("查找 " + target + " 失败");
            }
        }
    }
}
```

**Python代码**：
```python
import math

class BlockSearch:
    def __init__(self, array):
        self.data = array.copy()
        # 计算块的大小，通常取数据量的平方根
        self.block_size = int(math.sqrt(len(array)))
        # 计算块的数量
        self.block_count = (len(array) + self.block_size - 1) // self.block_size  # 向上取整
        
        # 构建索引表 [(start, max)]
        self.index = []
        self._build_index()
    
    def _build_index(self):
        """构建索引表"""
        for i in range(self.block_count):
            start = i * self.block_size
            end = min(start + self.block_size, len(self.data))
            
            # 找出块内最大值
            block_max = max(self.data[start:end])
            self.index.append((start, block_max))
    
    def search(self, target):
        """查找目标值，返回索引位置，未找到返回-1"""
        # 1. 在索引表中查找目标值可能在的块
        block_index = -1
        for i in range(self.block_count):
            if target <= self.index[i][1]:
                block_index = i
                break
        
        # 目标值大于所有块的最大值，不存在
        if block_index == -1:
            return -1
        
        # 2. 在对应的块内进行顺序查找
        start, _ = self.index[block_index]
        end = min(start + self.block_size, len(self.data))
        
        for i in range(start, end):
            if self.data[i] == target:
                return i  # 返回目标值在原始数组中的索引
        
        return -1  # 目标值不存在
    
    def print_blocks(self):
        """打印分块信息"""
        print("分块信息：")
        for i in range(self.block_count):
            start, max_val = self.index[i]
            end = min(start + self.block_size, len(self.data))
            print(f"块{i+1} [{start}-{end-1}]: {' '.join(map(str, self.data[start:end]))} (最大值: {max_val})")

# 测试代码
if __name__ == "__main__":
    array = [22, 12, 13, 8, 9, 20, 33, 42, 44, 38, 24, 48, 60, 58, 74, 49, 86, 53]
    searcher = BlockSearch(array)
    
    # 打印分块信息
    searcher.print_blocks()
    
    # 测试查找
    targets = [13, 42, 50, 86]
    for target in targets:
        index = searcher.search(target)
        if index != -1:
            print(f"查找 {target} 成功，索引位置：{index}")
        else:
            print(f"查找 {target} 失败")
```

**C++代码**：
```cpp
#include <iostream>
#include <vector>
#include <cmath>
#include <algorithm>
using namespace std;

struct Index {
    int start;  // 块的起始位置
    int max;    // 块内最大值
    
    Index(int s, int m) : start(s), max(m) {}
};

class BlockSearch {
private:
    vector<int> data;      // 原始数据数组
    vector<Index> index;   // 索引表
    int blockSize;         // 块的大小
    int blockCount;        // 块的数量
    
    void buildIndex() {
        index.clear();
        
        for (int i = 0; i < blockCount; i++) {
            int start = i * blockSize;
            int end = min(start + blockSize, (int)data.size());
            
            // 找出块内最大值
            int maxVal = data[start];
            for (int j = start + 1; j < end; j++) {
                if (data[j] > maxVal) {
                    maxVal = data[j];
                }
            }
            
            index.push_back(Index(start, maxVal));
        }
    }
    
public:
    BlockSearch(const vector<int>& array) : data(array) {
        // 计算块的大小
        blockSize = static_cast<int>(sqrt(data.size()));
        // 计算块的数量
        blockCount = (data.size() + blockSize - 1) / blockSize;  // 向上取整
        
        buildIndex();
    }
    
    int search(int target) {
        // 1. 在索引表中查找目标值可能在的块
        int blockIndex = -1;
        for (int i = 0; i < blockCount; i++) {
            if (target <= index[i].max) {
                blockIndex = i;
                break;
            }
        }
        
        // 目标值大于所有块的最大值，不存在
        if (blockIndex == -1) {
            return -1;
        }
        
        // 2. 在对应的块内进行顺序查找
        int start = index[blockIndex].start;
        int end = min(start + blockSize, (int)data.size());
        
        for (int i = start; i < end; i++) {
            if (data[i] == target) {
                return i;  // 返回目标值在原始数组中的索引
            }
        }
        
        return -1;  // 目标值不存在
    }
    
    void printBlocks() {
        cout << "分块信息：" << endl;
        for (int i = 0; i < blockCount; i++) {
            int start = index[i].start;
            int end = min(start + blockSize, (int)data.size());
            cout << "块" << (i + 1) << " [" << start << "-" << (end - 1) << "]: ";
            for (int j = start; j < end; j++) {
                cout << data[j] << " ";
            }
            cout << " (最大值: " << index[i].max << ")" << endl;
        }
    }
};

int main() {
    vector<int> array = {22, 12, 13, 8, 9, 20, 33, 42, 44, 38, 24, 48, 60, 58, 74, 49, 86, 53};
    BlockSearch searcher(array);
    
    // 打印分块信息
    searcher.printBlocks();
    
    // 测试查找
    vector<int> targets = {13, 42, 50, 86};
    for (int target : targets) {
        int index = searcher.search(target);
        if (index != -1) {
            cout << "查找 " << target << " 成功，索引位置：" << index << endl;
        } else {
            cout << "查找 " << target << " 失败" << endl;
        }
    }
    
    return 0;
}
```

### 2. 有序数组的分块查找优化

**题目来源**：数据结构进阶题

**题目描述**：对于有序数组，优化分块查找算法，使其效率更高

**解题思路**：
- 对于有序数组，可以优化索引表的结构
- 索引表可以使用二分查找来提高查找效率
- 块内查找也可以使用二分查找

**代码实现**：

**Java代码**：
```java
import java.util.Arrays;

public class OptimizedBlockSearch {
    // 索引表：每个元素存储块的起始位置、结束位置和最大值
    static class Index {
        int start;  // 块的起始位置
        int end;    // 块的结束位置
        int min;    // 块内最小值
        int max;    // 块内最大值
        
        Index(int start, int end, int min, int max) {
            this.start = start;
            this.end = end;
            this.min = min;
            this.max = max;
        }
    }
    
    private int[] data;       // 原始数据数组（已排序）
    private Index[] index;    // 索引表
    private int blockSize;    // 块的大小
    private int blockCount;   // 块的数量
    
    // 构造函数，初始化分块查找结构
    public OptimizedBlockSearch(int[] array) {
        // 确保数组已排序
        this.data = Arrays.copyOf(array, array.length);
        Arrays.sort(this.data);
        
        // 计算块的大小，通常取数据量的平方根
        this.blockSize = (int) Math.sqrt(array.length);
        // 计算块的数量
        this.blockCount = (array.length + blockSize - 1) / blockSize;  // 向上取整
        
        // 构建索引表
        buildIndex();
    }
    
    // 构建索引表
    private void buildIndex() {
        index = new Index[blockCount];
        
        for (int i = 0; i < blockCount; i++) {
            int start = i * blockSize;
            int end = Math.min(start + blockSize - 1, data.length - 1);
            
            // 由于数组已排序，块的最小值和最大值就是块的第一个和最后一个元素
            int min = data[start];
            int max = data[end];
            
            index[i] = new Index(start, end, min, max);
        }
    }
    
    // 查找函数
    public int search(int target) {
        // 1. 在索引表中使用二分查找找到目标值可能在的块
        int left = 0;
        int right = blockCount - 1;
        int blockIndex = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (target >= index[mid].min && target <= index[mid].max) {
                blockIndex = mid;
                break;
            } else if (target < index[mid].min) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        // 目标值不在任何块的范围内
        if (blockIndex == -1) {
            return -1;
        }
        
        // 2. 在对应的块内使用二分查找
        int start = index[blockIndex].start;
        int end = index[blockIndex].end;
        
        while (start <= end) {
            int mid = start + (end - start) / 2;
            if (data[mid] == target) {
                return mid;
            } else if (data[mid] < target) {
                start = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        
        return -1;  // 目标值不存在
    }
    
    // 打印分块信息
    public void printBlocks() {
        System.out.println("分块信息（已排序数组）：");
        System.out.println("原始数组: " + Arrays.toString(data));
        for (int i = 0; i < blockCount; i++) {
            System.out.print("块" + (i + 1) + " [" + index[i].start + "-" + index[i].end + "]: ");
            for (int j = index[i].start; j <= index[i].end; j++) {
                System.out.print(data[j] + " ");
            }
            System.out.println(" (min: " + index[i].min + ", max: " + index[i].max + ")");
        }
    }
    
    public static void main(String[] args) {
        int[] array = {22, 12, 13, 8, 9, 20, 33, 42, 44, 38, 24, 48, 60, 58, 74, 49, 86, 53};
        OptimizedBlockSearch searcher = new OptimizedBlockSearch(array);
        
        // 打印分块信息
        searcher.printBlocks();
        
        // 测试查找
        int[] targets = {13, 42, 50, 86, 53};
        for (int target : targets) {
            int index = searcher.search(target);
            if (index != -1) {
                System.out.println("查找 " + target + " 成功，索引位置：" + index + "，值：" + searcher.data[index]);
            } else {
                System.out.println("查找 " + target + " 失败");
            }
        }
    }
}
```

**Python代码**：
```python
import math

class OptimizedBlockSearch:
    def __init__(self, array):
        # 确保数组已排序
        self.data = sorted(array)
        # 计算块的大小，通常取数据量的平方根
        self.block_size = int(math.sqrt(len(self.data)))
        # 计算块的数量
        self.block_count = (len(self.data) + self.block_size - 1) // self.block_size  # 向上取整
        
        # 构建索引表 [{'start': start, 'end': end, 'min': min, 'max': max}]
        self.index = []
        self._build_index()
    
    def _build_index(self):
        """构建索引表"""
        for i in range(self.block_count):
            start = i * self.block_size
            end = min(start + self.block_size - 1, len(self.data) - 1)
            
            # 由于数组已排序，块的最小值和最大值就是块的第一个和最后一个元素
            block_info = {
                'start': start,
                'end': end,
                'min': self.data[start],
                'max': self.data[end]
            }
            self.index.append(block_info)
    
    def search(self, target):
        """查找目标值，返回索引位置，未找到返回-1"""
        # 1. 在索引表中使用二分查找找到目标值可能在的块
        left, right = 0, self.block_count - 1
        block_index = -1
        
        while left <= right:
            mid = left + (right - left) // 2
            if self.index[mid]['min'] <= target <= self.index[mid]['max']:
                block_index = mid
                break
            elif target < self.index[mid]['min']:
                right = mid - 1
            else:
                left = mid + 1
        
        # 目标值不在任何块的范围内
        if block_index == -1:
            return -1
        
        # 2. 在对应的块内使用二分查找
        start = self.index[block_index]['start']
        end = self.index[block_index]['end']
        
        while start <= end:
            mid = start + (end - start) // 2
            if self.data[mid] == target:
                return mid
            elif self.data[mid] < target:
                start = mid + 1
            else:
                end = mid - 1
        
        return -1  # 目标值不存在
    
    def print_blocks(self):
        """打印分块信息"""
        print("分块信息（已排序数组）：")
        print(f"原始数组: {self.data}")
        for i in range(self.block_count):
            idx = self.index[i]
            start, end = idx['start'], idx['end']
            block_data = self.data[start:end+1]
            print(f"块{i+1} [{start}-{end}]: {' '.join(map(str, block_data))} (min: {idx['min']}, max: {idx['max']})")

# 测试代码
if __name__ == "__main__":
    array = [22, 12, 13, 8, 9, 20, 33, 42, 44, 38, 24, 48, 60, 58, 74, 49, 86, 53]
    searcher = OptimizedBlockSearch(array)
    
    # 打印分块信息
    searcher.print_blocks()
    
    # 测试查找
    targets = [13, 42, 50, 86, 53]
    for target in targets:
        index = searcher.search(target)
        if index != -1:
            print(f"查找 {target} 成功，索引位置：{index}，值：{searcher.data[index]}")
        else:
            print(f"查找 {target} 失败")
```

**C++代码**：
```cpp
#include <iostream>
#include <vector>
#include <cmath>
#include <algorithm>
using namespace std;

struct Index {
    int start;  // 块的起始位置
    int end;    // 块的结束位置
    int min;    // 块内最小值
    int max;    // 块内最大值
    
    Index(int s, int e, int mi, int ma) : start(s), end(e), min(mi), max(ma) {}
};

class OptimizedBlockSearch {
private:
    vector<int> data;      // 原始数据数组（已排序）
    vector<Index> index;   // 索引表
    int blockSize;         // 块的大小
    int blockCount;        // 块的数量
    
    void buildIndex() {
        index.clear();
        
        for (int i = 0; i < blockCount; i++) {
            int start = i * blockSize;
            int end = min(start + blockSize - 1, (int)data.size() - 1);
            
            // 由于数组已排序，块的最小值和最大值就是块的第一个和最后一个元素
            int minVal = data[start];
            int maxVal = data[end];
            
            index.push_back(Index(start, end, minVal, maxVal));
        }
    }
    
public:
    OptimizedBlockSearch(const vector<int>& array) : data(array) {
        // 确保数组已排序
        sort(data.begin(), data.end());
        
        // 计算块的大小
        blockSize = static_cast<int>(sqrt(data.size()));
        // 计算块的数量
        blockCount = (data.size() + blockSize - 1) / blockSize;  // 向上取整
        
        buildIndex();
    }
    
    int search(int target) {
        // 1. 在索引表中使用二分查找找到目标值可能在的块
        int left = 0;
        int right = blockCount - 1;
        int blockIndex = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (target >= index[mid].min && target <= index[mid].max) {
                blockIndex = mid;
                break;
            } else if (target < index[mid].min) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        // 目标值不在任何块的范围内
        if (blockIndex == -1) {
            return -1;
        }
        
        // 2. 在对应的块内使用二分查找
        int start = index[blockIndex].start;
        int end = index[blockIndex].end;
        
        while (start <= end) {
            int mid = start + (end - start) / 2;
            if (data[mid] == target) {
                return mid;
            } else if (data[mid] < target) {
                start = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        
        return -1;  // 目标值不存在
    }
    
    void printBlocks() {
        cout << "分块信息（已排序数组）：" << endl;
        cout << "原始数组: [";
        for (size_t i = 0; i < data.size(); i++) {
            cout << data[i];
            if (i < data.size() - 1) cout << ", ";
        }
        cout << "]" << endl;
        
        for (int i = 0; i < blockCount; i++) {
            cout << "块" << (i + 1) << " [" << index[i].start << "-" << index[i].end << "]: ";
            for (int j = index[i].start; j <= index[i].end; j++) {
                cout << data[j] << " ";
            }
            cout << " (min: " << index[i].min << ", max: " << index[i].max << ")" << endl;
        }
    }
    
    // 获取已排序的数据数组（用于测试）
    const vector<int>& getData() const {
        return data;
    }
};

int main() {
    vector<int> array = {22, 12, 13, 8, 9, 20, 33, 42, 44, 38, 24, 48, 60, 58, 74, 49, 86, 53};
    OptimizedBlockSearch searcher(array);
    
    // 打印分块信息
    searcher.printBlocks();
    
    // 测试查找
    vector<int> targets = {13, 42, 50, 86, 53};
    for (int target : targets) {
        int index = searcher.search(target);
        if (index != -1) {
            cout << "查找 " << target << " 成功，索引位置：" << index << "，值：" << searcher.getData()[index] << endl;
        } else {
            cout << "查找 " << target << " 失败" << endl;
        }
    }
    
    return 0;
}
```

## 分块查找的应用场景

分块查找适用于以下场景：

1. **大型数据集**：当数据量很大，无法一次性加载到内存时
2. **部分有序数据**：当数据只是块间有序，块内无序时
3. **需要较快查找速度但又不想维护完全有序结构时**
4. **需要频繁更新但又需要保持较好查询性能时**
5. **数据库索引**：数据库中的索引结构常使用类似分块查找的思想

## 分块查找与其他查找算法的比较

| 算法 | 时间复杂度（平均） | 时间复杂度（最坏） | 空间复杂度 | 适用场景 |
|------|------------------|-------------------|-----------|----------|
| 顺序查找 | O(n) | O(n) | O(1) | 小规模数据，无序数据 |
| 二分查找 | O(log n) | O(log n) | O(1) | 有序数组 |
| 分块查找 | O(√n) | O(n) | O(√n) | 部分有序数据，需要较快查找 |
| 哈希查找 | O(1) | O(n) | O(n) | 可以构建哈希表的情况 |

## 经典算法题目

### 3. 多区间数据查询（离线分块处理）

**题目来源**：数据结构应用题

**题目描述**：给定一个数组，多次查询区间内的最大值

**解题思路**：
- 使用分块预处理，计算每个块的最大值
- 查询时，对于完整块直接取预处理的最大值，对于不完整块遍历查询

**代码实现**：

**Java代码**：
```java
import java.util.Arrays;

public class RangeMaxQuery {
    private int[] data;       // 原始数据数组
    private int[] blockMax;   // 每个块的最大值
    private int blockSize;    // 块的大小
    private int blockCount;   // 块的数量
    
    public RangeMaxQuery(int[] array) {
        this.data = Arrays.copyOf(array, array.length);
        this.blockSize = (int) Math.sqrt(array.length) + 1;  // +1防止除零
        this.blockCount = (array.length + blockSize - 1) / blockSize;
        this.blockMax = new int[blockCount];
        
        // 预处理每个块的最大值
        preprocess();
    }
    
    private void preprocess() {
        Arrays.fill(blockMax, Integer.MIN_VALUE);
        
        for (int i = 0; i < data.length; i++) {
            int blockIndex = i / blockSize;
            blockMax[blockIndex] = Math.max(blockMax[blockIndex], data[i]);
        }
    }
    
    // 查询区间[l, r]内的最大值（闭区间）
    public int queryMax(int l, int r) {
        if (l < 0 || r >= data.length || l > r) {
            throw new IllegalArgumentException("查询范围无效");
        }
        
        int maxVal = Integer.MIN_VALUE;
        int startBlock = l / blockSize;
        int endBlock = r / blockSize;
        
        // 如果查询范围在同一个块内
        if (startBlock == endBlock) {
            for (int i = l; i <= r; i++) {
                maxVal = Math.max(maxVal, data[i]);
            }
            return maxVal;
        }
        
        // 处理左边不完整的块
        for (int i = l; i < (startBlock + 1) * blockSize; i++) {
            maxVal = Math.max(maxVal, data[i]);
        }
        
        // 处理中间完整的块
        for (int i = startBlock + 1; i < endBlock; i++) {
            maxVal = Math.max(maxVal, blockMax[i]);
        }
        
        // 处理右边不完整的块
        for (int i = endBlock * blockSize; i <= r; i++) {
            maxVal = Math.max(maxVal, data[i]);
        }
        
        return maxVal;
    }
    
    public static void main(String[] args) {
        int[] array = {2, 1, 5, 3, 4, 7, 6, 8, 9, 10, 12, 11};
        RangeMaxQuery query = new RangeMaxQuery(array);
        
        System.out.println("原始数组: " + Arrays.toString(array));
        System.out.println("块大小: " + query.blockSize);
        System.out.println("块数量: " + query.blockCount);
        System.out.println("块最大值: " + Arrays.toString(query.blockMax));
        
        // 测试查询
        System.out.println("区间[0, 3]最大值: " + query.queryMax(0, 3));  // 5
        System.out.println("区间[2, 7]最大值: " + query.queryMax(2, 7));  // 8
        System.out.println("区间[4, 11]最大值: " + query.queryMax(4, 11));  // 12
        System.out.println("区间[6, 9]最大值: " + query.queryMax(6, 9));  // 10
    }
}
```

**Python代码**：
```python
import math

class RangeMaxQuery:
    def __init__(self, array):
        self.data = array.copy()
        self.block_size = int(math.sqrt(len(array))) + 1  # +1防止除零
        self.block_count = (len(array) + self.block_size - 1) // self.block_size
        self.block_max = [-float('inf')] * self.block_count
        
        # 预处理每个块的最大值
        self._preprocess()
    
    def _preprocess(self):
        """预处理每个块的最大值"""
        for i in range(len(self.data)):
            block_index = i // self.block_size
            self.block_max[block_index] = max(self.block_max[block_index], self.data[i])
    
    def query_max(self, l, r):
        """查询区间[l, r]内的最大值（闭区间）"""
        if l < 0 or r >= len(self.data) or l > r:
            raise ValueError("查询范围无效")
        
        max_val = -float('inf')
        start_block = l // self.block_size
        end_block = r // self.block_size
        
        # 如果查询范围在同一个块内
        if start_block == end_block:
            for i in range(l, r + 1):
                max_val = max(max_val, self.data[i])
            return max_val
        
        # 处理左边不完整的块
        for i in range(l, (start_block + 1) * self.block_size):
            max_val = max(max_val, self.data[i])
        
        # 处理中间完整的块
        for i in range(start_block + 1, end_block):
            max_val = max(max_val, self.block_max[i])
        
        # 处理右边不完整的块
        for i in range(end_block * self.block_size, r + 1):
            max_val = max(max_val, self.data[i])
        
        return max_val

# 测试代码
if __name__ == "__main__":
    array = [2, 1, 5, 3, 4, 7, 6, 8, 9, 10, 12, 11]
    query = RangeMaxQuery(array)
    
    print(f"原始数组: {array}")
    print(f"块大小: {query.block_size}")
    print(f"块数量: {query.block_count}")
    print(f"块最大值: {query.block_max}")
    
    # 测试查询
    print(f"区间[0, 3]最大值: {query.query_max(0, 3)}")  # 5
    print(f"区间[2, 7]最大值: {query.query_max(2, 7)}")  # 8
    print(f"区间[4, 11]最大值: {query.query_max(4, 11)}")  # 12
    print(f"区间[6, 9]最大值: {query.query_max(6, 9)}")  # 10
```

**C++代码**：
```cpp
#include <iostream>
#include <vector>
#include <cmath>
#include <climits>
using namespace std;

class RangeMaxQuery {
private:
    vector<int> data;      // 原始数据数组
    vector<int> blockMax;  // 每个块的最大值
    int blockSize;         // 块的大小
    int blockCount;        // 块的数量
    
    void preprocess() {
        blockMax.assign(blockCount, INT_MIN);
        
        for (int i = 0; i < (int)data.size(); i++) {
            int blockIndex = i / blockSize;
            blockMax[blockIndex] = max(blockMax[blockIndex], data[i]);
        }
    }
    
public:
    RangeMaxQuery(const vector<int>& array) : data(array) {
        blockSize = static_cast<int>(sqrt(array.size())) + 1;  // +1防止除零
        blockCount = (array.size() + blockSize - 1) / blockSize;
        
        preprocess();
    }
    
    // 查询区间[l, r]内的最大值（闭区间）
    int queryMax(int l, int r) {
        if (l < 0 || r >= (int)data.size() || l > r) {
            throw invalid_argument("查询范围无效");
        }
        
        int maxVal = INT_MIN;
        int startBlock = l / blockSize;
        int endBlock = r / blockSize;
        
        // 如果查询范围在同一个块内
        if (startBlock == endBlock) {
            for (int i = l; i <= r; i++) {
                maxVal = max(maxVal, data[i]);
            }
            return maxVal;
        }
        
        // 处理左边不完整的块
        for (int i = l; i < (startBlock + 1) * blockSize; i++) {
            maxVal = max(maxVal, data[i]);
        }
        
        // 处理中间完整的块
        for (int i = startBlock + 1; i < endBlock; i++) {
            maxVal = max(maxVal, blockMax[i]);
        }
        
        // 处理右边不完整的块
        for (int i = endBlock * blockSize; i <= r; i++) {
            maxVal = max(maxVal, data[i]);
        }
        
        return maxVal;
    }
    
    // 获取相关参数用于测试
    int getBlockSize() const { return blockSize; }
    int getBlockCount() const { return blockCount; }
    const vector<int>& getData() const { return data; }
    const vector<int>& getBlockMax() const { return blockMax; }
};

int main() {
    vector<int> array = {2, 1, 5, 3, 4, 7, 6, 8, 9, 10, 12, 11};
    RangeMaxQuery query(array);
    
    cout << "原始数组: [";
    for (size_t i = 0; i < array.size(); i++) {
        cout << array[i];
        if (i < array.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
    
    cout << "块大小: " << query.getBlockSize() << endl;
    cout << "块数量: " << query.getBlockCount() << endl;
    
    cout << "块最大值: [";
    const vector<int>& blockMax = query.getBlockMax();
    for (size_t i = 0; i < blockMax.size(); i++) {
        cout << blockMax[i];
        if (i < blockMax.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
    
    // 测试查询
    cout << "区间[0, 3]最大值: " << query.queryMax(0, 3) << endl;  // 5
    cout << "区间[2, 7]最大值: " << query.queryMax(2, 7) << endl;  // 8
    cout << "区间[4, 11]最大值: " << query.queryMax(4, 11) << endl;  // 12
    cout << "区间[6, 9]最大值: " << query.queryMax(6, 9) << endl;  // 10
    
    return 0;
}
```

## 更多练习题目

### 4. LeetCode 307. 区域和检索 - 数组可修改

**题目链接**：https://leetcode.com/problems/range-sum-query-mutable/

**题目描述**：给你一个数组 nums ，请你完成两类查询，其中一类是更新数组某个位置的值，另一类是求数组某个区间内元素的和。

**解题思路**：
- 使用分块方法，每个块维护自己的和
- 更新操作时，更新原数组和对应块的和
- 查询操作时，累加区间内完整块的和和不完整块的元素和

**Java代码**：
```java
class NumArray {
    private int[] nums;      // 原始数组
    private int[] blockSum;  // 每个块的和
    private int blockSize;   // 块的大小
    
    public NumArray(int[] nums) {
        this.nums = nums;
        this.blockSize = (int) Math.sqrt(nums.length) + 1;
        this.blockSum = new int[(nums.length + blockSize - 1) / blockSize];
        
        // 初始化块和
        for (int i = 0; i < nums.length; i++) {
            blockSum[i / blockSize] += nums[i];
        }
    }
    
    public void update(int index, int val) {
        int blockIndex = index / blockSize;
        blockSum[blockIndex] += (val - nums[index]);
        nums[index] = val;
    }
    
    public int sumRange(int left, int right) {
        int sum = 0;
        int startBlock = left / blockSize;
        int endBlock = right / blockSize;
        
        if (startBlock == endBlock) {
            // 在同一个块内
            for (int i = left; i <= right; i++) {
                sum += nums[i];
            }
            return sum;
        }
        
        // 左边不完整块
        for (int i = left; i < (startBlock + 1) * blockSize; i++) {
            sum += nums[i];
        }
        
        // 中间完整块
        for (int i = startBlock + 1; i < endBlock; i++) {
            sum += blockSum[i];
        }
        
        // 右边不完整块
        for (int i = endBlock * blockSize; i <= right; i++) {
            sum += nums[i];
        }
        
        return sum;
    }
}
```

**Python代码**：
```python
import math

class NumArray:
    def __init__(self, nums: List[int]):
        self.nums = nums
        self.block_size = int(math.sqrt(len(nums))) + 1
        self.block_count = (len(nums) + self.block_size - 1) // self.block_size
        self.block_sum = [0] * self.block_count
        
        # 初始化块和
        for i in range(len(nums)):
            self.block_sum[i // self.block_size] += nums[i]
    
    def update(self, index: int, val: int) -> None:
        block_index = index // self.block_size
        self.block_sum[block_index] += (val - self.nums[index])
        self.nums[index] = val
    
    def sumRange(self, left: int, right: int) -> int:
        sum_val = 0
        start_block = left // self.block_size
        end_block = right // self.block_size
        
        if start_block == end_block:
            # 在同一个块内
            for i in range(left, right + 1):
                sum_val += self.nums[i]
            return sum_val
        
        # 左边不完整块
        for i in range(left, (start_block + 1) * self.block_size):
            sum_val += self.nums[i]
        
        # 中间完整块
        for i in range(start_block + 1, end_block):
            sum_val += self.block_sum[i]
        
        # 右边不完整块
        for i in range(end_block * self.block_size, right + 1):
            sum_val += self.nums[i]
        
        return sum_val
```

**C++代码**：
```cpp
class NumArray {
private:
    vector<int> nums;      // 原始数组
    vector<int> blockSum;  // 每个块的和
    int blockSize;         // 块的大小
    
public:
    NumArray(vector<int>& nums) : nums(nums) {
        blockSize = static_cast<int>(sqrt(nums.size())) + 1;
        int blockCount = (nums.size() + blockSize - 1) / blockSize;
        blockSum.resize(blockCount, 0);
        
        // 初始化块和
        for (int i = 0; i < (int)nums.size(); i++) {
            blockSum[i / blockSize] += nums[i];
        }
    }
    
    void update(int index, int val) {
        int blockIndex = index / blockSize;
        blockSum[blockIndex] += (val - nums[index]);
        nums[index] = val;
    }
    
    int sumRange(int left, int right) {
        int sum = 0;
        int startBlock = left / blockSize;
        int endBlock = right / blockSize;
        
        if (startBlock == endBlock) {
            // 在同一个块内
            for (int i = left; i <= right; i++) {
                sum += nums[i];
            }
            return sum;
        }
        
        // 左边不完整块
        for (int i = left; i < (startBlock + 1) * blockSize; i++) {
            sum += nums[i];
        }
        
        // 中间完整块
        for (int i = startBlock + 1; i < endBlock; i++) {
            sum += blockSum[i];
        }
        
        // 右边不完整块
        for (int i = endBlock * blockSize; i <= right; i++) {
            sum += nums[i];
        }
        
        return sum;
    }
};
```

### 5. LeetCode 325. 和等于 k 的最长子数组长度

**题目链接**：https://leetcode.com/problems/maximum-size-subarray-sum-equals-k/

**题目描述**：给定一个数组和一个目标值 k，找到和等于 k 的最长连续子数组长度。如果不存在任意一个符合要求的子数组，则返回 0。

**解题思路**：
- 使用前缀和和哈希表优化
- 虽然这道题主要是前缀和思想，但可以结合分块思想进行预处理

**Java代码**：
```java
class Solution {
    public int maxSubArrayLen(int[] nums, int k) {
        Map<Long, Integer> prefixSumMap = new HashMap<>();
        long prefixSum = 0;
        int maxLength = 0;
        
        // 初始值，前缀和为0出现在索引-1处
        prefixSumMap.put(0L, -1);
        
        for (int i = 0; i < nums.length; i++) {
            prefixSum += nums[i];
            
            // 查找是否存在前缀和为prefixSum - k
            if (prefixSumMap.containsKey(prefixSum - k)) {
                maxLength = Math.max(maxLength, i - prefixSumMap.get(prefixSum - k));
            }
            
            // 只保存第一次出现的前缀和，以保证子数组最长
            if (!prefixSumMap.containsKey(prefixSum)) {
                prefixSumMap.put(prefixSum, i);
            }
        }
        
        return maxLength;
    }
}
```

**Python代码**：
```python
class Solution:
    def maxSubArrayLen(self, nums: List[int], k: int) -> int:
        prefix_sum_map = {0: -1}  # 前缀和为0出现在索引-1处
        prefix_sum = 0
        max_length = 0
        
        for i, num in enumerate(nums):
            prefix_sum += num
            
            # 查找是否存在前缀和为prefix_sum - k
            if prefix_sum - k in prefix_sum_map:
                max_length = max(max_length, i - prefix_sum_map[prefix_sum - k])
            
            # 只保存第一次出现的前缀和，以保证子数组最长
            if prefix_sum not in prefix_sum_map:
                prefix_sum_map[prefix_sum] = i
        
        return max_length
```

**C++代码**：
```cpp
class Solution {
public:
    int maxSubArrayLen(vector<int>& nums, int k) {
        unordered_map<long long, int> prefixSumMap;
        long long prefixSum = 0;
        int maxLength = 0;
        
        // 初始值，前缀和为0出现在索引-1处
        prefixSumMap[0] = -1;
        
        for (int i = 0; i < (int)nums.size(); i++) {
            prefixSum += nums[i];
            
            // 查找是否存在前缀和为prefixSum - k
            if (prefixSumMap.find(prefixSum - k) != prefixSumMap.end()) {
                maxLength = max(maxLength, i - prefixSumMap[prefixSum - k]);
            }
            
            // 只保存第一次出现的前缀和，以保证子数组最长
            if (prefixSumMap.find(prefixSum) == prefixSumMap.end()) {
                prefixSumMap[prefixSum] = i;
            }
        }
        
        return maxLength;
    }
};
```

## 分块算法的扩展应用

1. **莫队算法**：基于分块的离线查询优化算法
2. **块状链表**：将链表分块以提高随机访问性能
3. **块状数组**：用于处理大规模数据的动态数组
4. **二维分块**：用于二维区间查询和更新
5. **分块哈希**：用于字符串匹配等场景

## 总结

分块查找是一种重要的查找算法，它通过将数据分成多个块并建立索引，在保持一定查找效率的同时，也保持了较好的灵活性。它特别适用于处理大规模数据集和部分有序数据的场景。

在实际应用中，分块的思想不仅限于查找操作，还广泛应用于各种算法和数据结构中，如莫队算法、块状链表等。掌握分块思想对于解决复杂的算法问题具有重要意义。