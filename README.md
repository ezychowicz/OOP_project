# Darwin World
![Simulation interface](assets/simulation.gif)

## Authors
- Emil Żychowicz
- Jan Sarba

## Project description
Darwin World is a life simulation project built in Java. The simulation models an evolving ecosystem where two types of entities exist: animals and grass. Animals must consume grass to survive and reproduce, while grass grows dynamically on the map. 
The simulation is highly customizable, allowing users to adjust various parameters to observe different evolutionary scenarios.
[Full description](https://github.com/Ki3mONo/DarwinWorldSimulator/blob/main/Tresc_zadania/Readme.md) 

## Key Features
1. Simulation Cycle – Each day in the simulation includes:
- removing dead animals
- stochastic grass growth
- animal movement based on genetic traits
- animal reproduction (if conditions are met)
- energy consumption and regeneration through food
- Fertile Lands Variant – A map variation where grass has a higher probability of growing near existing plants, simulating fertile zones.
Key features are tested in unit/integration tests.
## Functionality
- detailed simulation statistics – Track both overall ecosystem metrics and individual animal statistics
- map & genetics variants – Experiment with different mutation settings and map configurations
- fertility visualization – Highlight regions where grass is more likely to grow
- CSV data export – Save simulation statistics for further analysis
- JavaFX-Based UI – Interactive graphical interface for enhanced user experience
- reproduction system – Animals that meet specific energy requirements can reproduce when they share the same position
- grass consumption – Eating grass increases an animal’s energy, allowing it to survive longer and reproduce.

## Interface Features
- animal Tracking – Click on an animal to follow its individual statistics.
- customizable Simulation Parameters – Adjust map size, energy levels, grass growth rate, and other settings.
- energy Graphs – View real-time energy fluctuations of a chosen animal.
- simulation Control – Pause and resume the simulation at any time.

## Technologies Used
- Java – Core language for simulation logic and backend.
- JavaFX – Used for building the graphical interface.
