# Contributing to Xentari

## Branch Naming Convention

All branches must follow this pattern:

```
<type>/<short-description>
```

### Types

| Type | Purpose | Example |
|------|---------|---------|
| `feature` | New feature or service | `feature/order-service` |
| `fix` | Bug fix | `fix/inventory-reserve-race` |
| `refactor` | Code restructuring | `refactor/event-payloads` |
| `docs` | Documentation only | `docs/api-reference` |
| `chore` | Build, CI, dependencies | `chore/docker-healthcheck` |
| `hotfix` | Urgent production fix | `hotfix/payment-timeout` |

### Rules

- Use lowercase with hyphens (kebab-case)
- Keep descriptions under 50 characters
- One concern per branch
- Always branch from `main`

---

## Commit Message Guidelines

Follow the [Conventional Commits](https://www.conventionalcommits.org/) specification.

### Format

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Type

| Type | Description |
|------|-------------|
| `feat` | New feature |
| `fix` | Bug fix |
| `docs` | Documentation |
| `style` | Formatting, no logic change |
| `refactor` | Code restructuring |
| `test` | Adding or updating tests |
| `chore` | Build, tooling, dependencies |

### Scope

The service or module affected: `order-service`, `inventory-service`, `gateway`, `docker`, `eureka`, etc.

### Subject

- Imperative mood ("add", not "added")
- No period at the end
- Max 72 characters

### Examples

```
feat(order-service): add order placement endpoint
fix(inventory-service): handle insufficient stock correctly
docs(readme): add architecture diagram
chore(docker): add healthchecks for postgres and rabbitmq
refactor(payment-service): extract payment gateway abstraction
test(order-service): add unit tests for order status transitions
```

---

## Pull Request Template

Every PR must use the template at `.github/PULL_REQUEST_TEMPLATE.md`.

### PR Rules

- One feature or fix per PR
- All CI checks must pass
- At least one approval required (self-review for solo projects)
- Link the related issue if applicable
- Squash merge into `main`

### PR Title

Follow the same format as commit messages:

```
feat(order-service): implement order placement flow
```

---

## Development Workflow

```
main
  └── feature/order-service
        └── [commits]
        └── PR → main (squash merge)
```

1. Create a branch from `main`
2. Make changes in focused commits
3. Push and open a PR
4. Review, approve, squash merge
5. Delete the branch after merge

---

## Code Standards

- Java 21
- Spring Boot 3.4.x
- Maven wrapper for builds
- Follow existing package structure: `com.xentari.<service>.<layer>`
- No comments unless absolutely necessary
- Meaningful variable and method names
