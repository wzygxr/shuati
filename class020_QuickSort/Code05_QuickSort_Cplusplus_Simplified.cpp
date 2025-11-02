// C++版本快速排序实现（简化版）
// 包含多种快速排序的实现方式

/*
 * 快速排序算法实现与应用
 * 
 * 本文件包含多种快速排序的实现方式及其在不同场景下的应用
 * 
 * 算法复杂度分析:
 * 时间复杂度:
 *   - 最好情况: O(n log n) - 每次划分都能将数组平均分成两部分
 *   - 平均情况: O(n log n) - 随机选择基准值的情况下
 *   - 最坏情况: O(n^2) - 每次选择的基准值都是最大或最小值
 * 空间复杂度:
 *   - O(log n) - 递归调用栈的深度
 * 
 * 算法优化策略:
 * 1. 随机选择基准值 - 避免最坏情况的出现
 * 2. 三路快排 - 处理重复元素较多的情况
 * 3. 小数组使用插入排序 - 减少递归开销
 * 4. 尾递归优化 - 减少栈空间使用
 */

// 基础快速排序实现
void quickSort1(int arr[], int l, int r) {
    if (l >= r) return;
    
    // 随机选择基准值
    int i = l + (rand() % (r - l + 1));
    
    // 将随机选择的基准值交换到第一个位置
    int temp = arr[l];
    arr[l] = arr[i];
    arr[i] = temp;
    
    // 基准值
    int pivot = arr[l];
    
    // 双指针分区
    int left = l, right = r;
    
    while (left < right) {
        // 从右向左找小于基准值的元素
        while (left < right && arr[right] >= pivot) right--;
        
        // 从左向右找大于基准值的元素
        while (left < right && arr[left] <= pivot) left++;
        
        // 交换元素
        if (left < right) {
            temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
        }
    }
    
    // 将基准值放到正确位置
    temp = arr[l];
    arr[l] = arr[left];
    arr[left] = temp;
    
    // 递归排序左右两部分
    quickSort1(arr, l, left - 1);
    quickSort1(arr, left + 1, r);
}

// 三路快速排序实现（处理重复元素）
void quickSort2(int arr[], int l, int r) {
    if (l >= r) return;
    
    // 随机选择基准值
    int i = l + (rand() % (r - l + 1));
    int temp = arr[l];
    arr[l] = arr[i];
    arr[i] = temp;
    
    // 基准值
    int pivot = arr[l];
    
    // 三路分区指针
    int lt = l;      // 小于区域的右边界
    int gt = r + 1;  // 大于区域的左边界
    int idx = l + 1; // 当前处理元素的索引
    
    // 三路分区过程
    while (idx < gt) {
        if (arr[idx] < pivot) {
            // 当前元素小于基准值，将其交换到小于区域
            temp = arr[++lt];
            arr[lt] = arr[idx];
            arr[idx++] = temp;
        } else if (arr[idx] > pivot) {
            // 当前元素大于基准值，将其交换到大于区域
            temp = arr[--gt];
            arr[gt] = arr[idx];
            arr[idx] = temp;
            // 注意这里idx不自增，因为交换过来的元素还未处理
        } else {
            // 当前元素等于基准值，保持在等于区域
            idx++;
        }
    }
    
    // 将基准值放到等于区域的左边界
    temp = arr[l];
    arr[l] = arr[lt];
    arr[lt] = temp;
    
    // 递归排序小于区域和大于区域
    quickSort2(arr, l, lt - 1);
    quickSort2(arr, gt, r);
}

// 快速选择算法（用于查找第k小元素）
int quickSelect(int arr[], int l, int r, int k) {
    // 递归终止条件
    if (l >= r) return arr[l];
    
    // 随机选择基准值
    int i = l + (rand() % (r - l + 1));
    
    // 将基准值交换到末尾位置
    int temp = arr[i];
    arr[i] = arr[r];
    arr[r] = temp;
    
    // 基准值
    int pivot = arr[r];
    
    // 小于等于基准值区域的右边界
    int left = l;
    
    // 遍历数组，将小于等于基准值的元素放到左侧
    for (int j = l; j < r; j++) {
        if (arr[j] <= pivot) {
            temp = arr[left];
            arr[left] = arr[j];
            arr[j] = temp;
            left++;
        }
    }
    
    // 将基准值放到正确位置
    temp = arr[left];
    arr[left] = arr[r];
    arr[r] = temp;
    
    // 根据基准值位置决定下一步操作
    if (left == k) {
        // 如果基准值位置正好是目标位置，直接返回
        return arr[left];
    } else if (left < k) {
        // 如果基准值位置小于目标位置，在右半部分继续查找
        return quickSelect(arr, left + 1, r, k);
    } else {
        // 如果基准值位置大于目标位置，在左半部分继续查找
        return quickSelect(arr, l, left - 1, k);
    }
}

// 插入排序算法，用于小数组优化
void insertionSort(int arr[], int l, int r) {
    // 从第二个元素开始，逐个插入到已排序序列中
    for (int i = l + 1; i <= r; i++) {
        int key = arr[i]; // 当前要插入的元素
        int j = i - 1;    // 已排序序列的最后一个位置
        
        // 在已排序序列中找到合适的插入位置
        while (j >= l && arr[j] > key) {
            arr[j + 1] = arr[j]; // 元素后移
            j--;
        }
        
        // 插入元素
        arr[j + 1] = key;
    }
}

// 优化版本的快速排序（小数组使用插入排序）
void quickSortOptimized(int arr[], int l, int r) {
    // 小数组阈值
    const int THRESHOLD = 15;
    
    // 对小数组使用插入排序
    if (r - l <= THRESHOLD) {
        insertionSort(arr, l, r);
        return;
    }
    
    // 对大数组继续使用快速排序
    int i = l + (rand() % (r - l + 1));
    int temp = arr[l];
    arr[l] = arr[i];
    arr[i] = temp;
    
    int pivot = arr[l];
    int lt = l;      // 小于区域的右边界
    int gt = r + 1;  // 大于区域的左边界
    int idx = l + 1; // 当前处理元素的索引
    
    while (idx < gt) {
        if (arr[idx] < pivot) {
            temp = arr[++lt];
            arr[lt] = arr[idx];
            arr[idx++] = temp;
        } else if (arr[idx] > pivot) {
            temp = arr[--gt];
            arr[gt] = arr[idx];
            arr[idx] = temp;
        } else {
            idx++;
        }
    }
    
    temp = arr[l];
    arr[l] = arr[lt];
    arr[lt] = temp;
    
    quickSortOptimized(arr, l, lt - 1);
    quickSortOptimized(arr, gt, r);
}

// 查找第k大的元素
int findKthLargest(int arr[], int size, int k) {
    // 第k大元素等价于第size-k小的元素
    return quickSelect(arr, 0, size - 1, size - k);
}

// 颜色分类（三路快排应用）
void sortColors(int nums[], int size) {
    // 三路快排思想：0放左边，1放中间，2放右边
    int zero = -1;      // [0...zero] == 0
    int two = size;     // [two...n-1] == 2
    int i = 0;          // 当前处理的位置
    
    while (i < two) {
        if (nums[i] == 0) {
            // 当前元素为0，交换到0区域的下一个位置
            int temp = nums[++zero];
            nums[zero] = nums[i];
            nums[i++] = temp;
        } else if (nums[i] == 1) {
            // 当前元素为1，保持在中间区域
            i++;
        } else { // nums[i] == 2
            // 当前元素为2，交换到2区域的前一个位置
            int temp = nums[i];
            nums[i] = nums[--two];
            nums[two] = temp;
        }
    }
}

// 移动零（分区思想应用）
void moveZeroes(int nums[], int size) {
    // 双指针：将非零元素移动到数组前面
    int nonZeroPos = 0; // 指向下一个非零元素应该放的位置
    
    // 第一次遍历：将所有非零元素移到前面
    for (int i = 0; i < size; i++) {
        if (nums[i] != 0) {
            if (i != nonZeroPos) {
                int temp = nums[i];
                nums[i] = nums[nonZeroPos];
                nums[nonZeroPos] = temp;
            }
            nonZeroPos++;
        }
    }
}