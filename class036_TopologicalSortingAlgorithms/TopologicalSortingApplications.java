package class059;

import java.util.*;

/**
 * 拓扑排序在实际工程中的应用案例
 * 
 * 本文件展示拓扑排序在各种实际场景中的应用：
 * 1. 任务调度系统
 * 2. 构建系统
 * 3. 包依赖管理
 * 4. 数据流水线
 * 5. 工作流引擎
 * 6. 课程安排系统
 * 7. 项目管理系统
 */

public class TopologicalSortingApplications {

    /**
     * =====================================================================
     * 应用1：任务调度系统
     * 
     * 场景：分布式任务调度，任务之间存在依赖关系
     * 需求：确定任务的执行顺序，避免循环依赖
     */
    public static class TaskScheduler {
        private Map<String, Task> tasks;
        private Map<String, List<String>> dependencies;
        
        public static class Task {
            String id;
            String name;
            int priority;
            long estimatedTime;
            
            public Task(String id, String name, int priority, long estimatedTime) {
                this.id = id;
                this.name = name;
                this.priority = priority;
                this.estimatedTime = estimatedTime;
            }
        }
        
        public TaskScheduler() {
            this.tasks = new HashMap<>();
            this.dependencies = new HashMap<>();
        }
        
        /**
         * 添加任务
         */
        public void addTask(Task task) {
            tasks.put(task.id, task);
            dependencies.put(task.id, new ArrayList<>());
        }
        
        /**
         * 添加任务依赖
         */
        public void addDependency(String taskId, String dependsOnTaskId) {
            if (!tasks.containsKey(taskId) || !tasks.containsKey(dependsOnTaskId)) {
                throw new IllegalArgumentException("任务不存在");
            }
            dependencies.get(taskId).add(dependsOnTaskId);
        }
        
        /**
         * 获取任务执行顺序
         */
        public List<Task> getExecutionOrder() {
            // 构建图
            Map<String, Integer> inDegree = new HashMap<>();
            Map<String, List<String>> graph = new HashMap<>();
            
            // 初始化
            for (String taskId : tasks.keySet()) {
                inDegree.put(taskId, 0);
                graph.put(taskId, new ArrayList<>());
            }
            
            // 构建依赖关系图
            for (Map.Entry<String, List<String>> entry : dependencies.entrySet()) {
                String taskId = entry.getKey();
                for (String depTaskId : entry.getValue()) {
                    graph.get(depTaskId).add(taskId);
                    inDegree.put(taskId, inDegree.get(taskId) + 1);
                }
            }
            
            // 拓扑排序
            Queue<String> queue = new LinkedList<>();
            for (String taskId : inDegree.keySet()) {
                if (inDegree.get(taskId) == 0) {
                    queue.offer(taskId);
                }
            }
            
            List<Task> executionOrder = new ArrayList<>();
            while (!queue.isEmpty()) {
                String currentTaskId = queue.poll();
                executionOrder.add(tasks.get(currentTaskId));
                
                for (String nextTaskId : graph.get(currentTaskId)) {
                    inDegree.put(nextTaskId, inDegree.get(nextTaskId) - 1);
                    if (inDegree.get(nextTaskId) == 0) {
                        queue.offer(nextTaskId);
                    }
                }
            }
            
            // 检查循环依赖
            if (executionOrder.size() != tasks.size()) {
                throw new IllegalStateException("存在循环依赖，无法调度任务");
            }
            
            return executionOrder;
        }
        
        /**
         * 带优先级的任务调度
         */
        public List<Task> getPriorityBasedOrder() {
            Map<String, Integer> inDegree = new HashMap<>();
            Map<String, List<String>> graph = new HashMap<>();
            
            // 初始化
            for (String taskId : tasks.keySet()) {
                inDegree.put(taskId, 0);
                graph.put(taskId, new ArrayList<>());
            }
            
            // 构建图
            for (Map.Entry<String, List<String>> entry : dependencies.entrySet()) {
                String taskId = entry.getKey();
                for (String depTaskId : entry.getValue()) {
                    graph.get(depTaskId).add(taskId);
                    inDegree.put(taskId, inDegree.get(taskId) + 1);
                }
            }
            
            // 使用优先队列（按优先级）
            PriorityQueue<String> queue = new PriorityQueue<>(
                (a, b) -> Integer.compare(tasks.get(b).priority, tasks.get(a).priority)
            );
            
            for (String taskId : inDegree.keySet()) {
                if (inDegree.get(taskId) == 0) {
                    queue.offer(taskId);
                }
            }
            
            List<Task> executionOrder = new ArrayList<>();
            while (!queue.isEmpty()) {
                String currentTaskId = queue.poll();
                executionOrder.add(tasks.get(currentTaskId));
                
                for (String nextTaskId : graph.get(currentTaskId)) {
                    inDegree.put(nextTaskId, inDegree.get(nextTaskId) - 1);
                    if (inDegree.get(nextTaskId) == 0) {
                        queue.offer(nextTaskId);
                    }
                }
            }
            
            return executionOrder;
        }
    }

    /**
     * =====================================================================
     * 应用2：构建系统（如Maven、Gradle）
     * 
     * 场景：源代码编译，模块之间存在依赖关系
     * 需求：确定编译顺序，避免循环依赖
     */
    public static class BuildSystem {
        private Map<String, Module> modules;
        private Map<String, List<String>> dependencies;
        
        public static class Module {
            String name;
            String path;
            List<String> sourceFiles;
            List<String> dependencies;
            
            public Module(String name, String path) {
                this.name = name;
                this.path = path;
                this.sourceFiles = new ArrayList<>();
                this.dependencies = new ArrayList<>();
            }
        }
        
        public BuildSystem() {
            this.modules = new HashMap<>();
            this.dependencies = new HashMap<>();
        }
        
        /**
         * 添加模块
         */
        public void addModule(Module module) {
            modules.put(module.name, module);
            dependencies.put(module.name, new ArrayList<>(module.dependencies));
        }
        
        /**
         * 获取构建顺序
         */
        public List<Module> getBuildOrder() {
            Map<String, Integer> inDegree = new HashMap<>();
            Map<String, List<String>> graph = new HashMap<>();
            
            // 初始化
            for (String moduleName : modules.keySet()) {
                inDegree.put(moduleName, 0);
                graph.put(moduleName, new ArrayList<>());
            }
            
            // 构建依赖图
            for (Map.Entry<String, List<String>> entry : dependencies.entrySet()) {
                String moduleName = entry.getKey();
                for (String depModule : entry.getValue()) {
                    graph.get(depModule).add(moduleName);
                    inDegree.put(moduleName, inDegree.get(moduleName) + 1);
                }
            }
            
            // 拓扑排序
            Queue<String> queue = new LinkedList<>();
            for (String moduleName : inDegree.keySet()) {
                if (inDegree.get(moduleName) == 0) {
                    queue.offer(moduleName);
                }
            }
            
            List<Module> buildOrder = new ArrayList<>();
            while (!queue.isEmpty()) {
                String currentModule = queue.poll();
                buildOrder.add(modules.get(currentModule));
                
                for (String nextModule : graph.get(currentModule)) {
                    inDegree.put(nextModule, inDegree.get(nextModule) - 1);
                    if (inDegree.get(nextModule) == 0) {
                        queue.offer(nextModule);
                    }
                }
            }
            
            if (buildOrder.size() != modules.size()) {
                throw new IllegalStateException("存在循环依赖，无法构建");
            }
            
            return buildOrder;
        }
        
        /**
         * 增量构建：只构建受影响的模块
         */
        public List<Module> getIncrementalBuildOrder(String changedModule) {
            List<Module> fullBuildOrder = getBuildOrder();
            
            // 找到受影响的模块
            Set<String> affectedModules = new HashSet<>();
            Queue<String> affectedQueue = new LinkedList<>();
            affectedQueue.offer(changedModule);
            affectedModules.add(changedModule);
            
            while (!affectedQueue.isEmpty()) {
                String current = affectedQueue.poll();
                for (Map.Entry<String, List<String>> entry : dependencies.entrySet()) {
                    if (entry.getValue().contains(current) && !affectedModules.contains(entry.getKey())) {
                        affectedModules.add(entry.getKey());
                        affectedQueue.offer(entry.getKey());
                    }
                }
            }
            
            // 过滤构建顺序
            List<Module> incrementalOrder = new ArrayList<>();
            for (Module module : fullBuildOrder) {
                if (affectedModules.contains(module.name)) {
                    incrementalOrder.add(module);
                }
            }
            
            return incrementalOrder;
        }
    }

    /**
     * =====================================================================
     * 应用3：包依赖管理（如npm、pip）
     * 
     * 场景：软件包安装，包之间存在依赖关系
     * 需求：确定安装顺序，解决版本冲突
     */
    public static class PackageManager {
        private Map<String, Package> packages;
        private Map<String, List<Dependency>> dependencies;
        
        public static class Package {
            String name;
            String version;
            String description;
            
            public Package(String name, String version) {
                this.name = name;
                this.version = version;
            }
        }
        
        public static class Dependency {
            String packageName;
            String versionConstraint;
            
            public Dependency(String packageName, String versionConstraint) {
                this.packageName = packageName;
                this.versionConstraint = versionConstraint;
            }
        }
        
        public PackageManager() {
            this.packages = new HashMap<>();
            this.dependencies = new HashMap<>();
        }
        
        /**
         * 添加包
         */
        public void addPackage(Package pkg) {
            packages.put(pkg.name + "@" + pkg.version, pkg);
            dependencies.put(pkg.name + "@" + pkg.version, new ArrayList<>());
        }
        
        /**
         * 添加依赖
         */
        public void addDependency(String packageId, Dependency dependency) {
            if (!dependencies.containsKey(packageId)) {
                throw new IllegalArgumentException("包不存在");
            }
            dependencies.get(packageId).add(dependency);
        }
        
        /**
         * 获取安装顺序
         */
        public List<Package> getInstallationOrder(String rootPackage) {
            Map<String, Integer> inDegree = new HashMap<>();
            Map<String, List<String>> graph = new HashMap<>();
            
            // 初始化
            for (String packageId : packages.keySet()) {
                inDegree.put(packageId, 0);
                graph.put(packageId, new ArrayList<>());
            }
            
            // 构建依赖图
            for (Map.Entry<String, List<Dependency>> entry : dependencies.entrySet()) {
                String packageId = entry.getKey();
                for (Dependency dep : entry.getValue()) {
                    // 简化处理：假设版本匹配
                    String depPackageId = findMatchingPackage(dep);
                    if (depPackageId != null) {
                        graph.get(depPackageId).add(packageId);
                        inDegree.put(packageId, inDegree.get(packageId) + 1);
                    }
                }
            }
            
            // 从根包开始拓扑排序
            Queue<String> queue = new LinkedList<>();
            if (inDegree.get(rootPackage) == 0) {
                queue.offer(rootPackage);
            }
            
            List<Package> installationOrder = new ArrayList<>();
            while (!queue.isEmpty()) {
                String currentPackageId = queue.poll();
                installationOrder.add(packages.get(currentPackageId));
                
                for (String nextPackageId : graph.get(currentPackageId)) {
                    inDegree.put(nextPackageId, inDegree.get(nextPackageId) - 1);
                    if (inDegree.get(nextPackageId) == 0) {
                        queue.offer(nextPackageId);
                    }
                }
            }
            
            return installationOrder;
        }
        
        private String findMatchingPackage(Dependency dependency) {
            // 简化实现：返回第一个匹配的包
            for (String packageId : packages.keySet()) {
                if (packageId.startsWith(dependency.packageName + "@")) {
                    return packageId;
                }
            }
            return null;
        }
        
        /**
         * 检测版本冲突
         */
        public boolean hasVersionConflicts() {
            Map<String, Set<String>> packageVersions = new HashMap<>();
            
            // 收集所有包的版本信息
            for (String packageId : packages.keySet()) {
                String[] parts = packageId.split("@");
                if (parts.length == 2) {
                    String name = parts[0];
                    String version = parts[1];
                    packageVersions.computeIfAbsent(name, k -> new HashSet<>()).add(version);
                }
            }
            
            // 检查版本冲突
            for (Set<String> versions : packageVersions.values()) {
                if (versions.size() > 1) {
                    return true;
                }
            }
            
            return false;
        }
    }

    /**
     * =====================================================================
     * 应用4：数据流水线（ETL流程）
     * 
     * 场景：数据处理流程，步骤之间存在依赖关系
     * 需求：确定处理顺序，优化资源使用
     */
    public static class DataPipeline {
        private Map<String, ProcessingStep> steps;
        private Map<String, List<String>> dependencies;
        
        public static class ProcessingStep {
            String id;
            String name;
            String type; // EXTRACT, TRANSFORM, LOAD
            int estimatedResource;
            
            public ProcessingStep(String id, String name, String type, int estimatedResource) {
                this.id = id;
                this.name = name;
                this.type = type;
                this.estimatedResource = estimatedResource;
            }
        }
        
        public DataPipeline() {
            this.steps = new HashMap<>();
            this.dependencies = new HashMap<>();
        }
        
        public void addStep(ProcessingStep step) {
            steps.put(step.id, step);
            dependencies.put(step.id, new ArrayList<>());
        }
        
        public void addDependency(String stepId, String dependsOnStepId) {
            dependencies.get(stepId).add(dependsOnStepId);
        }
        
        /**
         * 获取处理顺序
         */
        public List<ProcessingStep> getProcessingOrder() {
            Map<String, Integer> inDegree = new HashMap<>();
            Map<String, List<String>> graph = new HashMap<>();
            
            // 初始化
            for (String stepId : steps.keySet()) {
                inDegree.put(stepId, 0);
                graph.put(stepId, new ArrayList<>());
            }
            
            // 构建依赖图
            for (Map.Entry<String, List<String>> entry : dependencies.entrySet()) {
                String stepId = entry.getKey();
                for (String depStepId : entry.getValue()) {
                    graph.get(depStepId).add(stepId);
                    inDegree.put(stepId, inDegree.get(stepId) + 1);
                }
            }
            
            // 拓扑排序
            Queue<String> queue = new LinkedList<>();
            for (String stepId : inDegree.keySet()) {
                if (inDegree.get(stepId) == 0) {
                    queue.offer(stepId);
                }
            }
            
            List<ProcessingStep> processingOrder = new ArrayList<>();
            while (!queue.isEmpty()) {
                String currentStepId = queue.poll();
                processingOrder.add(steps.get(currentStepId));
                
                for (String nextStepId : graph.get(currentStepId)) {
                    inDegree.put(nextStepId, inDegree.get(nextStepId) - 1);
                    if (inDegree.get(nextStepId) == 0) {
                        queue.offer(nextStepId);
                    }
                }
            }
            
            return processingOrder;
        }
        
        /**
         * 资源优化调度
         */
        public List<ProcessingStep> getResourceOptimizedOrder(int maxConcurrent) {
            List<ProcessingStep> basicOrder = getProcessingOrder();
            
            // 简单的资源优化：按类型分组，控制并发数
            Map<String, List<ProcessingStep>> stepsByType = new HashMap<>();
            for (ProcessingStep step : basicOrder) {
                stepsByType.computeIfAbsent(step.type, k -> new ArrayList<>()).add(step);
            }
            
            List<ProcessingStep> optimizedOrder = new ArrayList<>();
            int currentConcurrent = 0;
            
            for (ProcessingStep step : basicOrder) {
                if (currentConcurrent < maxConcurrent) {
                    optimizedOrder.add(step);
                    currentConcurrent++;
                } else {
                    // 简单的等待策略
                    optimizedOrder.add(step);
                }
            }
            
            return optimizedOrder;
        }
    }

    /**
     * =====================================================================
     * 应用5：工作流引擎
     * 
     * 场景：业务流程管理，活动之间存在顺序关系
     * 需求：确定活动执行顺序，支持并行分支
     */
    public static class WorkflowEngine {
        private Map<String, Activity> activities;
        private Map<String, List<Transition>> transitions;
        
        public static class Activity {
            String id;
            String name;
            String type; // START, END, TASK, DECISION
            Map<String, Object> properties;
            
            public Activity(String id, String name, String type) {
                this.id = id;
                this.name = name;
                this.type = type;
                this.properties = new HashMap<>();
            }
        }
        
        public static class Transition {
            String fromActivity;
            String toActivity;
            String condition; // 条件表达式
            
            public Transition(String fromActivity, String toActivity) {
                this.fromActivity = fromActivity;
                this.toActivity = toActivity;
            }
        }
        
        public WorkflowEngine() {
            this.activities = new HashMap<>();
            this.transitions = new HashMap<>();
        }
        
        public void addActivity(Activity activity) {
            activities.put(activity.id, activity);
            transitions.put(activity.id, new ArrayList<>());
        }
        
        public void addTransition(Transition transition) {
            transitions.get(transition.fromActivity).add(transition);
        }
        
        /**
         * 获取可能的执行路径
         */
        public List<List<Activity>> getPossiblePaths() {
            // 简化实现：返回所有拓扑排序结果
            Map<String, Integer> inDegree = new HashMap<>();
            Map<String, List<String>> graph = new HashMap<>();
            
            // 初始化
            for (String activityId : activities.keySet()) {
                inDegree.put(activityId, 0);
                graph.put(activityId, new ArrayList<>());
            }
            
            // 构建图
            for (List<Transition> transitionList : transitions.values()) {
                for (Transition transition : transitionList) {
                    graph.get(transition.fromActivity).add(transition.toActivity);
                    inDegree.put(transition.toActivity, inDegree.get(transition.toActivity) + 1);
                }
            }
            
            // 使用DFS获取所有拓扑排序
            List<List<Activity>> allPaths = new ArrayList<>();
            dfsTopologicalSort(new ArrayList<>(), new HashSet<>(), inDegree, graph, allPaths);
            
            return allPaths;
        }
        
        private void dfsTopologicalSort(List<Activity> currentPath, Set<String> visited, 
                                       Map<String, Integer> inDegree, Map<String, List<String>> graph,
                                       List<List<Activity>> allPaths) {
            if (currentPath.size() == activities.size()) {
                allPaths.add(new ArrayList<>(currentPath));
                return;
            }
            
            for (String activityId : activities.keySet()) {
                if (!visited.contains(activityId) && inDegree.get(activityId) == 0) {
                    visited.add(activityId);
                    currentPath.add(activities.get(activityId));
                    
                    // 更新入度
                    Map<String, Integer> tempInDegree = new HashMap<>(inDegree);
                    for (String next : graph.get(activityId)) {
                        tempInDegree.put(next, tempInDegree.get(next) - 1);
                    }
                    
                    dfsTopologicalSort(currentPath, visited, tempInDegree, graph, allPaths);
                    
                    // 回溯
                    currentPath.remove(currentPath.size() - 1);
                    visited.remove(activityId);
                }
            }
        }
    }

    /**
     * =====================================================================
     * 应用6：课程安排系统
     * 
     * 场景：大学课程安排，课程之间存在先修关系
     * 需求：确定学习顺序，满足先修条件
     */
    public static class CourseSchedulingSystem {
        private Map<String, Course> courses;
        private Map<String, List<String>> prerequisites;
        
        public static class Course {
            String code;
            String name;
            int credits;
            String semester; // FALL, SPRING, SUMMER
            int level; // 100, 200, 300, 400
            
            public Course(String code, String name, int credits, String semester, int level) {
                this.code = code;
                this.name = name;
                this.credits = credits;
                this.semester = semester;
                this.level = level;
            }
        }
        
        public CourseSchedulingSystem() {
            this.courses = new HashMap<>();
            this.prerequisites = new HashMap<>();
        }
        
        public void addCourse(Course course) {
            courses.put(course.code, course);
            prerequisites.put(course.code, new ArrayList<>());
        }
        
        public void addPrerequisite(String courseCode, String prerequisiteCode) {
            prerequisites.get(courseCode).add(prerequisiteCode);
        }
        
        /**
         * 获取可行的学习计划
         */
        public Map<Integer, List<Course>> getStudyPlan(int maxCreditsPerSemester) {
            Map<String, Integer> inDegree = new HashMap<>();
            Map<String, List<String>> graph = new HashMap<>();
            
            // 初始化
            for (String courseCode : courses.keySet()) {
                inDegree.put(courseCode, 0);
                graph.put(courseCode, new ArrayList<>());
            }
            
            // 构建先修关系图
            for (Map.Entry<String, List<String>> entry : prerequisites.entrySet()) {
                String courseCode = entry.getKey();
                for (String preCode : entry.getValue()) {
                    graph.get(preCode).add(courseCode);
                    inDegree.put(courseCode, inDegree.get(courseCode) + 1);
                }
            }
            
            // 按学期安排课程
            Map<Integer, List<Course>> studyPlan = new HashMap<>();
            int currentSemester = 1;
            int currentCredits = 0;
            
            while (!inDegree.isEmpty()) {
                List<String> availableCourses = new ArrayList<>();
                for (String courseCode : inDegree.keySet()) {
                    if (inDegree.get(courseCode) == 0) {
                        availableCourses.add(courseCode);
                    }
                }
                
                if (availableCourses.isEmpty()) {
                    throw new IllegalStateException("存在循环先修关系");
                }
                
                // 按学分和级别排序
                availableCourses.sort((a, b) -> {
                    Course courseA = courses.get(a);
                    Course courseB = courses.get(b);
                    if (courseA.level != courseB.level) {
                        return Integer.compare(courseA.level, courseB.level);
                    }
                    return Integer.compare(courseA.credits, courseB.credits);
                });
                
                List<Course> semesterCourses = new ArrayList<>();
                for (String courseCode : availableCourses) {
                    Course course = courses.get(courseCode);
                    if (currentCredits + course.credits <= maxCreditsPerSemester) {
                        semesterCourses.add(course);
                        currentCredits += course.credits;
                        inDegree.remove(courseCode);
                        
                        // 更新后续课程的入度
                        for (String nextCourse : graph.get(courseCode)) {
                            inDegree.put(nextCourse, inDegree.get(nextCourse) - 1);
                        }
                    }
                }
                
                studyPlan.put(currentSemester, semesterCourses);
                currentSemester++;
                currentCredits = 0;
            }
            
            return studyPlan;
        }
    }

    /**
     * =====================================================================
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 拓扑排序实际应用测试 ===");
        
        // 测试任务调度系统
        TaskScheduler scheduler = new TaskScheduler();
        scheduler.addTask(new TaskScheduler.Task("T1", "数据预处理", 1, 1000));
        scheduler.addTask(new TaskScheduler.Task("T2", "特征工程", 2, 2000));
        scheduler.addDependency("T2", "T1");
        System.out.println("任务调度顺序: " + scheduler.getExecutionOrder().size());
        
        // 测试构建系统
        BuildSystem buildSystem = new BuildSystem();
        BuildSystem.Module moduleA = new BuildSystem.Module("module-a", "/path/a");
        BuildSystem.Module moduleB = new BuildSystem.Module("module-b", "/path/b");
        moduleB.dependencies.add("module-a");
        buildSystem.addModule(moduleA);
        buildSystem.addModule(moduleB);
        System.out.println("构建顺序: " + buildSystem.getBuildOrder().size());
        
        System.out.println("=== 应用测试完成 ===");
    }
}