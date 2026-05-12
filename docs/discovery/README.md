# Analysis & Brainstorming

⚠ Content is updated periodically to reflect the current state of the software architecture. ⚠

This folder holds the drafts and logical schemas that guided the design process.
It integrates research and design notes for both Front-end and Back-end, ensuring a cohesive architectural vision.
This space documents the "behind the scenes" of the development process, from the first sketch to the final
implementation.

# 🚀 Project Concept

The foundation of the application. This document was presented to the commission for the initial project approval.

* 📄 [Project_Concept_ITA.pdf](./Project_Concept_ITA.pdf) - *Original proposal (Italian).*

---

## Architecture Evolution

Here's how the data structure evolved during the design phase.

### Initial Draft

[Database_map1.jpg](Database_map1.jpg)
In this early stage, many entities were planned as simple `Enums` to speed up the first MVP.

* The focus was on the basic "Hook-Task-Reward" flow.
* Integrated a dedicated `User_Exp_Progress` table to handle complex gamification states.

### Phase 2: Refined ER Diagram (Current)

[Database_map2.jpg](./Database_map2.jpg)

* Switched from Enums to **relational tables** for better scalability.
* Implemented **UUIDs** for all primary keys to enhance security.

---