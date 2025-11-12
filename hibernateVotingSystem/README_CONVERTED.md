# Online Voting System (Hibernate + Console)

Converted from the original supermarket project.

## How to run

```bash
mvn -q -e -DskipTests exec:java -Dexec.mainClass=voting.VotingApp
```

This uses an embedded H2 database file at `./votingdb`.

## Features
- Register voters (unique by email)
- Add candidates
- Create elections (title + date)
- Cast vote (enforces one vote per voter per election)
- Show results per election