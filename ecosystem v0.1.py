from typing import Tuple

import tkinter as tk


# enum with the different types of enities
# that can be found in the ecosystem
class EntityType:
    VOID = -1
    ORGANISM = 0
    PLANT = 1
    OBSTACLE = 2


# void space of ecosystem
class Void:
    is_alive = False
    type = EntityType.VOID


# class of solid objects that form the surface of the field
class Obstacle:
    is_alive = False
    type = EntityType.OBSTACLE


# general class of ecosystem entities
class Entity:

    def __init__(self) -> None:
        # position in ecosystem field and type
        self.row: int
        self.col: int
        self.type: EntityType

        self.is_alive: bool

    def set_position(self, row: int, col: int) -> None:
        self.row = row
        self.col = col

    def get_position(self) -> Tuple[int, int]:
        return self.row, self.col

    def step(self) -> None:
        raise NotImplementedError


# class that represents the genetic code of organisms
# in dictionary form of keys (fields) and values (values)
class GeneticCode:

    def __init__(self, fields: dict) -> None:
        self.fields = fields

    def __init__(self, genes: 'GeneticCode') -> None:
        pass


# class of organisms that can move around the field
# reproduce, die, and evolve through genetical mutations
class Organism(Entity):

    def __init__(self) -> None:
        super().__init__()
        self.is_alive = True
        self.type = EntityType.ORGANISM


# class of plants that can reproduce and grow
# in areas with free space and light for photosynthesis
class Plant(Entity):

    def __init__(self) -> None:
        super().__init__()
        self.is_alive = True
        self.type = EntityType.PLANT


# class that represents an ecosystem's field and
# includes methods for working with it
class Field:

    def __init__(self, height, width) -> None:
        self.height = height
        self.width = width
        self.size = (height, width)

        self._field: list
        self.reset()

    # get all the entities in the field
    @property
    def enitites(self):
        enitites = []
        for row in self._field:
            for element in row:
                if isinstance(element, Entity):
                    enitites.append(element)
        return enitites

    # reset the field to its initial state
    # (all void spaces)
    def reset(self):
        self._field = [[Void() for _ in range(self.width)] for _ in range(self.height)]

    # get state of the field
    def get_state(self):
        return self._field


# core of the ecosystem
# includes methods for working with the ecosystem
# simulating its evolution, and checking its state
class Ecosystem:

    def __init__(self, width, height, speed=1) -> None:
        self.height = height
        self.width = width
        self.speed = speed

        self.field = Field(height, width)

    def step(self):
        # iterate over all entities in the ecosystem
        # and call their step method
        for entity in self.field.entities:
            # if entity is alive, call its step method
            if entity.is_alive:
                entity.step()

    # simulate the ecosystem's steps count
    def simulate(self, steps=1):
        for _ in range(steps):
            self.step()
        
    # get state of the field
    def get_state(self):
        return self.field.get_state()


# visualization of the ecosystem
class GraphicalRepresentation:

    def __init__(self, ecosystem, window_width, window_height) -> None:
        self.ecosystem: Ecosystem = ecosystem

        self.height = ecosystem.height
        self.width = ecosystem.width

        # create window and set title
        self.root = tk.Tk()
        self.root.title('Ecosystem')

        # calculate size of one cell in pixels based on window size
        self.cell_size = min(window_width / self.width,
                             window_height / self.height)

        # create canvas and set its size
        self.canvas = tk.Canvas(self.root,
                                width=self.width * self.cell_size,
                                height=self.height * self.cell_size)
        self.canvas.pack()
        self.canvas.focus_set()
        

    def draw(self):
        state = self.ecosystem.get_state()

        # iterate over all cells in the field
        # and draw them depending on their type
        for row in range(self.ecosystem.height):
            for col in range(self.ecosystem.width):
                entity = state[row][col]

                if entity.type == EntityType.VOID:
                    self.canvas.create_rectangle(
                        col * self.cell_size,
                        row * self.cell_size,
                        (col + 1) * self.cell_size,
                        (row + 1) * self.cell_size,
                        fill='black')

    def start(self):
        self.root.mainloop()
        self.draw()
                    
if __name__ == '__main__':
    ecosystem = Ecosystem(12, 10)
    sim = GraphicalRepresentation(ecosystem, 1200, 700)
    sim.start()
