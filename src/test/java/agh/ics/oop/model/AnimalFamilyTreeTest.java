package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AnimalFamilyTreeTest {
    @Test
    void registerParentChildTest() {
        // Given
        AnimalFamilyTree familyTree = new AnimalFamilyTree();

        // When
        familyTree.registerParentChild(1, 2);
        familyTree.registerParentChild(1, 3);

        // Then
        Set<Integer> descendantsOfParent1 = familyTree.getDescendants(1);
        assertEquals(2, descendantsOfParent1.size());
        assertTrue(descendantsOfParent1.contains(2));
        assertTrue(descendantsOfParent1.contains(3));
    }
    @Test
    void getAllDescendantsTest() {
        // Given
        AnimalFamilyTree familyTree = new AnimalFamilyTree();
        familyTree.registerParentChild(1, 2);
        familyTree.registerParentChild(1, 3);
        familyTree.registerParentChild(2, 4);
        familyTree.registerParentChild(3, 5);

        // When
        Set<Integer> descendants = familyTree.getDescendants(1); // powinniśmy dostać 2, 3, 4, 5

        // Then
        assertEquals(4, descendants.size());
        assertTrue(descendants.contains(2));
        assertTrue(descendants.contains(3));
        assertTrue(descendants.contains(4));
        assertTrue(descendants.contains(5));
    }
    @Test
    void getDescendantsCountTest() {
        // Given
        AnimalFamilyTree familyTree = new AnimalFamilyTree();
        familyTree.registerParentChild(1, 2);
        familyTree.registerParentChild(1, 3);
        familyTree.registerParentChild(2, 4);
        familyTree.registerParentChild(3, 5);

        // When
        int descendantsCount = familyTree.getDescendantsCount(1);

        // Then
        assertEquals(4, descendantsCount);
    }

    @Test
    void registerSelfAsDescendantTest() {
        // Given
        AnimalFamilyTree familyTree = new AnimalFamilyTree();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> familyTree.registerParentChild(1, 1));
        assertEquals("Parent id and child id are the same", exception.getMessage()); // Potomków nie ma, ponieważ nie można dodać samego siebie
    }
}
