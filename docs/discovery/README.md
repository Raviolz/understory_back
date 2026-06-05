## Related repository

Frontend repository: [Understory Frontend](https://github.com/Raviolz/understory_front.git)

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

### Initial Draft 🔨

[Database_map1.jpg](Database_map1.jpg)
In this early stage, many entities were planned as simple `Enums` to speed up the first MVP.

* The focus was on the basic "Hook-Task-Reward" flow.
* Integrated a dedicated `User_Exp_Progress` table to handle complex gamification states.

### Refined ER Diagram

[Database_map2.jpg](./Database_map2.jpg)

* Switched from Enums to **relational tables** for better scalability.
* Implemented **UUIDs** for all primary keys to enhance security.
*

### Phase 3: Refined ER Diagram — Narrative Experience Model

[Database_map3.jpg](./Database_map3.jpg)

This iteration refines the relationship between physical locations, narrative content and user interactions.

* Moved categories from `Points_Of_Interest` to `Experiences`.

  Now `Points_Of_Interest` represents only the physical location. `Experiences` acts as the narrative and interactive
  layer connected to that place. This allows a single `Point_Of_Interest` to host multiple narrative angles, such as
  `HIDDEN_HISTORY` and `URBAN_MYSTERY`.
* Removed the separate `Experience_Journal_Entry` table. Journal content is now stored in `Experiences` and the user
  diary is built from completed progress records.
* Added `User_Upload_Submission` as a dedicated user interaction table, keeping uploaded images separate from general
  experience progress.
* Polished entity relationships, foreign keys and unique constraints to better match the application flow before moving
  into services and API development.