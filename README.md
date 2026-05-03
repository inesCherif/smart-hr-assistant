# Smart HR Assistant 

A distributed Java RMI application bringing **Natural Language to SQL** capabilities to HR data. 
Managers can ask questions in plain English, and the system automatically translates them to SQL via AI, queries the database, and returns the answers!

## Architecture Stack
Built seamlessly across 4 Docker containers:
- **Database**: MySQL 8.0 containing mocked HR records (employees, departments, absences, vacations).
- **RMI Registry**: Disconnected Java naming directory (`1099`).
- **Server Core**: Java 11 Server utilizing JDBC and native HTTP json mapping to connect the Database to the Groq LLM API.
- **Client**: Ultra-lightweight interactive RMI terminal client.

## Quick Start

Ensure Docker is running, then bring up the cluster:

```bash
# 1. Build and start the entire backend (MySQL, Registry, and Server)
docker-compose up -d --build

# 2. Attach to the interactive Client to start asking questions!
docker start hr_server
docker start -ai hr_client
```

## Example Queries

Once the client terminal attaches, try asking:
- *"How many employees work in IT?"*
- *"Who has the highest salary in the company?"*
- *"How many days of vacation did Alice Smith take?"*
- *"List all employees hired after 2020."*

##  Configuration
The server expects your LLM credentials securely inside `server/.env`. 
*(Note: Do not commit your `.env` file to version control)*
```env
GROQ_API_KEY=gsk_your_key_here
GROQ_API_URL=https://api.groq.com/openai/v1/chat/completions
GROQ_MODEL=gpt-oss-20b # Or any other open model you prefer
```

## Design Best Practices
- **Strict Microservice Isolation**: The Client has absolutely zero database or external API dependencies. All heavy lifting is seamlessly encapsulated in the Server Core.
- **Security Check**: The server is hardcoded to natively reject any dangerous non-`SELECT` SQL statements.
- **Dynamic Decoding**: Decodes varied tables blindly via `ResultSetMetaData` accommodating any type of analytic answer.
