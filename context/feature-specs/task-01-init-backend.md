# Task Specification: Scaffold Backend Modules (NestJS-Like Architecture)

## Objective

Scaffold the complete Spring Boot backend folder structure and initial components for all Phase 1 modules. The architecture must emulate a NestJS-like encapsulated module pattern (Package-by-Feature) to maximize decoupling and prepare for future microservices migration.

## Step 1: Context Priming (CRITICAL)

Before generating any files, you MUST strictly read `AGENTS.md` at the project root.

---

## Step 2: NestJS-Style Modular Architecture Blueprint

For every feature module detected in the database schema (`user`, `kyc`, `product`, `order`,...), you must strictly generate a self-contained package under `backend/src/main/java/vn/datnguy3n/marketplace/modules/[feature_name]/` with the following encapsulated structure:

## Step 3: Implementation Instructions

1. **Base Infrastructure:** Ensure `BaseEntity.java` (containing `id` + 6 audit fields) is generated first under `vn.datnguy3n.marketplace.common`.
2. **Scan & Generate All Modules:** Scan `context/architecture.md`, identify all base and translation tables, and map them into their respective module directories following the structural blueprint above.
3. **Dependency Rule:** Modules must be highly decoupled. Controllers must only talk to their own Services. If cross-module communication is needed, it must happen via Service-to-Service interaction, NEVER Controller-to-Repository or cross-module Repository injection.
4. **Code Stubs:** For this initialization task, generate all required `@RestController`, `@Service`, `Interface`, and `@Repository` files with clean, compilable placeholder code (stubs) and proper annotations (`@RequiredArgsConstructor`, `@Table`, etc.).

## Output Deliverables

1. A visual directory tree of the newly generated modular structure.
2. All compilable Java classes matching the blueprint for Phase 1 modules.
